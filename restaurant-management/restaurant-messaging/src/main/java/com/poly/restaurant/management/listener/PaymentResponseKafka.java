package com.poly.restaurant.management.listener;

import com.poly.kafka.consumer.KafkaConsumer;
import com.poly.restaurant.application.port.in.message.listener.PaymentResponseListener;
import com.poly.restaurant.management.mapper.PaymentMessageMapper;
import com.poly.restaurant.management.message.PaymentResponseMessageAvro;
import com.poly.restaurant.management.message.PaymentResponseMessage;
import com.poly.restaurant.management.helper.PaymentMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Kafka Listener chịu trách nhiệm lắng nghe và xử lý các response message từ Payment Service
 * về việc thanh toán trong Restaurant Service.
 * 
 * NGHIỆP VỤ:
 * - Nhận thông báo kết quả thanh toán từ Payment Service
 * - Xử lý các trạng thái thanh toán: COMPLETED, FAILED, CANCELLED
 * - Chuyển đổi Avro model thành domain entity
 * - Gửi thông tin payment đến PaymentResponseListener để xử lý business logic
 * 
 * PATTERNS ÁP DỤNG:
 * - Event-Driven Architecture: Lắng nghe payment response events
 * - Message Mapping: Chuyển đổi giữa Avro và domain models
 * - Separation of Concerns: Tách biệt message handling và business logic
 * - Error Handling: Xử lý các loại exception khác nhau
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseKafka implements KafkaConsumer<PaymentResponseMessageAvro> {

    private final PaymentMessageMapper paymentMessageMapper;
    private final PaymentResponseListener paymentResponseListener;

    /**
     * Lắng nghe và xử lý payment response messages từ Kafka topic
     * 
     * NGHIỆP VỤ:
     * - Nhận batch messages từ payment service
     * - Xử lý từng payment response event
     * - Đảm bảo reliable message processing với error handling
     *
     * @param messages   Danh sách PaymentResponseMessageAvro messages
     * @param keys       Kafka message keys
     * @param partitions Kafka partition numbers
     * @param offsets    Kafka message offsets
     */
    @Override
    @KafkaListener(topics = "${kafka.topic.payment-response}", id = "${kafka.group-id}")
    public void receive(@Payload List<PaymentResponseMessageAvro> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        
        // Validate input parameters
        validateInputParameters(messages, keys, partitions, offsets);

        // Log thông tin batch processing
        logBatchProcessingInfo(messages.size(), keys, partitions, offsets);

        // Process each payment response message
        processPaymentResponseMessages(messages);
    }

    /**
     * Validate các tham số đầu vào
     */
    private void validateInputParameters(List<PaymentResponseMessageAvro> messages,
                                         List<String> keys,
                                         List<Integer> partitions,
                                         List<Long> offsets) {
        Assert.notNull(messages, "Messages không được null");
        Assert.notNull(keys, "Keys không được null");
        Assert.notNull(partitions, "Partitions không được null");
        Assert.notNull(offsets, "Offsets không được null");
        Assert.isTrue(messages.size() == keys.size() && 
                     messages.size() == partitions.size() && 
                     messages.size() == offsets.size(),
                "Messages, keys, partitions và offsets phải có cùng kích thước");
    }

    /**
     * Log thông tin batch processing
     */
    private void logBatchProcessingInfo(int messageCount, List<String> keys, 
                                       List<Integer> partitions, List<Long> offsets) {
        log.info("Received {} payment response messages. Keys: {}, Partitions: {}, Offsets: {}",
                messageCount, keys, partitions, offsets);
    }

    /**
     * Xử lý danh sách payment response messages
     */
    private void processPaymentResponseMessages(List<PaymentResponseMessageAvro> messages) {
        messages.forEach(this::processSinglePaymentMessage);
    }

    /**
     * Xử lý một payment response message
     */
    private void processSinglePaymentMessage(PaymentResponseMessageAvro paymentResponseMessageAvro) {
        try {
            // Log thông tin payment đang xử lý
            logPaymentProcessingStart(paymentResponseMessageAvro);

            // Chuyển đổi Avro model thành domain message
            PaymentResponseMessage paymentResponseMessage = 
                    paymentMessageMapper.paymentResponseMessageAvroToPaymentResponseMessage(paymentResponseMessageAvro);

            // Xử lý theo payment status
            processPaymentByStatus(paymentResponseMessage);

            // Log thành công
            logPaymentProcessingSuccess(paymentResponseMessageAvro);

        } catch (Exception e) {
            // Log và xử lý lỗi
            handleProcessingError(paymentResponseMessageAvro, e);
        }
    }

    /**
     * Xử lý payment theo trạng thái
     */
    private void processPaymentByStatus(PaymentResponseMessage paymentResponseMessage) {
        String paymentStatus = paymentResponseMessage.getOrderPaymentStatus();

        if (PaymentMessageHelper.isPaymentSuccessful(paymentStatus)) {
            // Xử lý payment thành công
            paymentResponseListener.onPaymentSuccess(paymentResponseMessage);
        } else if (PaymentMessageHelper.isPaymentFailed(paymentStatus)) {
            // Xử lý payment thất bại hoặc bị hủy
            paymentResponseListener.onPaymentFailure(paymentResponseMessage);
        } else {
            // Xử lý trạng thái không xác định
            handleUnknownPaymentStatus(paymentResponseMessage, paymentStatus);
        }
    }

    /**
     * Xử lý trạng thái payment không xác định
     */
    private void handleUnknownPaymentStatus(PaymentResponseMessage paymentResponseMessage, String paymentStatus) {
        log.warn("Unknown payment status: {} for order id: {}. Skipping processing.",
                paymentStatus, paymentResponseMessage.getOrderId());
    }

    /**
     * Log thông tin bắt đầu xử lý payment
     */
    private void logPaymentProcessingStart(PaymentResponseMessageAvro paymentResponseMessageAvro) {
        log.info("Bắt đầu xử lý payment response cho order id: {}", 
                paymentResponseMessageAvro.getOrderId());
    }

    /**
     * Log thông tin xử lý payment thành công
     */
    private void logPaymentProcessingSuccess(PaymentResponseMessageAvro paymentResponseMessageAvro) {
        log.info("Payment response đã được xử lý thành công cho order id: {}", 
                paymentResponseMessageAvro.getOrderId());
    }

    /**
     * Xử lý lỗi khi xử lý message
     */
    private void handleProcessingError(PaymentResponseMessageAvro paymentResponseMessageAvro, Exception e) {
        log.error("Lỗi khi xử lý payment response cho order id: {}. Error: {}", 
                paymentResponseMessageAvro.getOrderId(), e.getMessage(), e);
    }
}
