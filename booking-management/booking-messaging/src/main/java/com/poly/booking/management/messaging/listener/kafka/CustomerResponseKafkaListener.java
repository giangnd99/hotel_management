package com.poly.booking.management.messaging.listener.kafka;

import com.poly.booking.management.domain.kafka.model.CustomerModelAvro;
import com.poly.booking.management.domain.port.in.message.listener.customer.CustomerMessageListener;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.kafka.consumer.KafkaConsumer;
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
 * Kafka Listener chịu trách nhiệm lắng nghe và xử lý các message từ Customer Service
 * khi có customer được tạo mới.
 * 
 * NGHIỆP VỤ:
 * - Nhận thông báo khi customer được tạo thành công từ Customer Service
 * - Chuyển đổi Avro model thành domain entity
 * - Gửi thông tin customer đến CustomerMessageListener để xử lý business logic
 * 
 * PATTERNS ÁP DỤNG:
 * - Event-Driven Architecture: Lắng nghe customer creation events
 * - Message Mapping: Chuyển đổi giữa Avro và domain models
 * - Separation of Concerns: Tách biệt message handling và business logic
 * 
 * FLOW XỬ LÝ:
 * 1. Nhận customer creation message từ Kafka topic
 * 2. Log thông tin message để tracking
 * 3. Chuyển đổi CustomerModelAvro thành CustomerCreatedMessageResponse
 * 4. Gửi đến CustomerMessageListener để xử lý business logic
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerResponseKafkaListener implements KafkaConsumer<CustomerModelAvro> {

    private final CustomerMessageListener customerMessageListener;
    private final BookingMessageDataMapper bookingDataMapper;

    /**
     * Lắng nghe và xử lý customer creation messages từ Kafka topic
     * 
     * NGHIỆP VỤ:
     * - Nhận batch messages từ customer service
     * - Xử lý từng customer creation event
     * - Đảm bảo reliable message processing
     * 
     * @param messages Danh sách CustomerModelAvro messages
     * @param keys Kafka message keys
     * @param partitions Kafka partition numbers
     * @param offsets Kafka message offsets
     */
    @Override
    @KafkaListener(topics = "${booking-service.customer-topic-name}", id = "${kafka-consumer-config.customer-group-id}")
    public void receive(@Payload List<CustomerModelAvro> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        
        // Validate input parameters
        validateInputParameters(messages, keys, partitions, offsets);
        
        // Log thông tin batch processing
        logBatchProcessingInfo(messages.size(), keys, partitions, offsets);
        
        // Process each customer creation message
        processCustomerCreationMessages(messages);
    }

    /**
     * Validate các tham số đầu vào
     * 
     * Đảm bảo tất cả thông tin cần thiết cho việc xử lý message đều hợp lệ
     */
    private void validateInputParameters(List<CustomerModelAvro> messages,
                                       List<String> keys,
                                       List<Integer> partitions,
                                       List<Long> offsets) {
        Assert.notNull(messages, "Messages list không được null");
        Assert.notNull(keys, "Keys list không được null");
        Assert.notNull(partitions, "Partitions list không được null");
        Assert.notNull(offsets, "Offsets list không được null");
        Assert.isTrue(messages.size() == keys.size(), "Messages và keys phải có cùng kích thước");
        Assert.isTrue(messages.size() == partitions.size(), "Messages và partitions phải có cùng kích thước");
        Assert.isTrue(messages.size() == offsets.size(), "Messages và offsets phải có cùng kích thước");
    }

    /**
     * Log thông tin batch processing
     * 
     * Ghi log chi tiết về việc xử lý batch messages để tracking và monitoring
     */
    private void logBatchProcessingInfo(int messageCount,
                                      List<String> keys,
                                      List<Integer> partitions,
                                      List<Long> offsets) {
        log.info("Nhận {} customer creation messages với keys: {}, partitions: {} và offsets: {}",
                messageCount,
                keys.toString(),
                partitions.toString(),
                offsets.toString());
    }

    /**
     * Xử lý từng customer creation message
     * 
     * Chuyển đổi Avro model thành domain entity và gửi đến business logic handler
     */
    private void processCustomerCreationMessages(List<CustomerModelAvro> messages) {
        messages.forEach(this::processSingleCustomerMessage);
    }

    /**
     * Xử lý một customer creation message
     * 
     * Chuyển đổi CustomerModelAvro thành CustomerCreatedMessageResponse
     * và gửi đến CustomerMessageListener
     */
    private void processSingleCustomerMessage(CustomerModelAvro customerModelAvro) {
        try {
            // Log thông tin customer đang xử lý
            logCustomerProcessingStart(customerModelAvro);
            
            // Chuyển đổi Avro model thành domain entity
            var customerCreatedMessage = bookingDataMapper.customerAvroToCustomerEntity(customerModelAvro);
            
            // Gửi đến business logic handler
            customerMessageListener.customerCreated(customerCreatedMessage);
            
            // Log thành công
            logCustomerProcessingSuccess(customerModelAvro);
            
        } catch (Exception e) {
            // Log và xử lý lỗi
            handleCustomerProcessingError(customerModelAvro, e);
        }
    }

    /**
     * Log thông tin bắt đầu xử lý customer
     */
    private void logCustomerProcessingStart(CustomerModelAvro customerModelAvro) {
        log.debug("Bắt đầu xử lý customer creation cho customer: {} ({} {})",
                customerModelAvro.getId(),
                customerModelAvro.getFirstName(),
                customerModelAvro.getLastName());
    }

    /**
     * Log thông tin xử lý customer thành công
     */
    private void logCustomerProcessingSuccess(CustomerModelAvro customerModelAvro) {
        log.info("Xử lý customer creation thành công cho customer: {} ({} {})",
                customerModelAvro.getId(),
                customerModelAvro.getFirstName(),
                customerModelAvro.getLastName());
    }

    /**
     * Xử lý lỗi khi xử lý customer message
     * 
     * Ghi log chi tiết lỗi và có thể thêm logic retry hoặc dead letter queue
     */
    private void handleCustomerProcessingError(CustomerModelAvro customerModelAvro, Exception exception) {
        log.error("Lỗi khi xử lý customer creation cho customer: {} ({} {}). Lỗi: {}",
                customerModelAvro.getId(),
                customerModelAvro.getFirstName(),
                customerModelAvro.getLastName(),
                exception.getMessage(),
                exception);
        
        // TODO: Có thể thêm logic retry hoặc dead letter queue ở đây
        // TODO: Có thể gửi notification đến admin về lỗi xử lý customer
        // TODO: Có thể implement circuit breaker pattern
    }
}
