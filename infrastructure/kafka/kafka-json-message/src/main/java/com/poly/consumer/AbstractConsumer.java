package com.poly.consumer;

import com.poly.dedup.DedupStore;
import com.poly.message.BaseMessage;
import com.poly.message.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

@Slf4j
public abstract class AbstractConsumer<T extends BaseMessage, R extends BaseResponse> {
    protected final DedupStore dedup;
    protected final long dedupTtlMs;
    protected final long responseCacheTtlMs;

    protected AbstractConsumer(DedupStore dedup, long dedupTtlMs, long responseCacheTtlMs) {
        this.dedup = dedup;
        this.dedupTtlMs = dedupTtlMs;
        this.responseCacheTtlMs = responseCacheTtlMs;
    }

    /**
     * Phương thức trừu tượng để lớp con cung cấp business key (idempotency key)
     * cho một message duy nhất.
     * Ví dụ: "PAYMENT:" + paymentId
     */
    protected abstract String idempotencyKey(T msg);

    /**
     * Phương thức trừu tượng chứa logic nghiệp vụ chính.
     * Nó nhận một message request và trả về một response (có thể là null).
     */
    protected abstract R handleBusiness(T request);

    /**
     * Phương thức xử lý chính, được gọi bởi @KafkaListener.
     * Xử lý một batch các message, đảm bảo mỗi message được xử lý độc lập.
     */
    public void handleBatch(@Payload List<T> messages,
                            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("Received a batch of {} messages. Keys: {}, Partitions: {}, Offsets: {}",
                messages.size(), keys, partitions, offsets);

        for (int i = 0; i < messages.size(); i++) {
            T message = messages.get(i);
            String key = keys.get(i);
            long offset = offsets.get(i);
            int partition = partitions.get(i);

            try {
                handleSingleMessage(message, key, offset, partition);
            } catch (Exception e) {
                // Ngoại lệ ném ra từ đây sẽ được DefaultErrorHandler bắt và gửi đến DLT.
                // Việc ném lại ngoại lệ là cách Spring Kafka xử lý lỗi mặc định.
                log.error("Error processing message. Topic: {}, Key: {}, Partition: {}, Offset: {}. Error: {}",
                        "topic-name-goes-here", key, partition, offset, e.getMessage(), e);
                // Với cơ chế DLQ đã có, chúng ta không cần xử lý lại lỗi ở đây.
                // Chỉ cần ném lại ngoại lệ và Spring Kafka sẽ lo phần còn lại.
                throw new RuntimeException("Error processing message. Message will be sent to DLT.", e);
            }
        }
    }

    /**
     * Phương thức nội bộ xử lý từng message đơn lẻ, bao gồm logic chống trùng lặp và cache.
     * Nó là lõi của logic consumer.
     */
    private R handleSingleMessage(T request, String key, long offset, int partition) throws Exception {
        log.info("Processing message. Key: {}, Partition: {}, Offset: {}, CorrelationId: {}",
                key, partition, offset, request.getCorrelationId());

        // 1. Kiểm tra response đã được cache hay chưa bằng CorrelationId
        if (request.getCorrelationId() != null) {
            R cachedResponse = (R) dedup.getResponse(request.getCorrelationId(), BaseResponse.class);
            if (cachedResponse != null) {
                log.info("Message with CorrelationId {} is a duplicate, returning cached response.", request.getCorrelationId());
                cachedResponse.setErrorMessage("DUPLICATE_RESPONSE");
                return cachedResponse;
            }
        }

        // 2. Chống trùng lặp bằng Idempotency Key
        String idempotencyKey = idempotencyKey(request);
        boolean isFirstMessage = dedup.markIfNotProcessed(idempotencyKey, dedupTtlMs);

        if (!isFirstMessage) {
            log.warn("Message with Idempotency Key {} is a duplicate and will be ignored.", idempotencyKey);
            return null; // Không xử lý tiếp
        }

        // 3. Gọi business logic
        log.info("New message received. Calling business handler for Idempotency Key: {}", idempotencyKey);
        R response = handleBusiness(request);

        // 4. Cache response nếu có CorrelationId
        if (response != null && request.getCorrelationId() != null) {
            dedup.putResponse(request.getCorrelationId(), response, responseCacheTtlMs);
            log.debug("Cached response for CorrelationId: {}", request.getCorrelationId());
        }

        return response;
    }
}
