package com.poly.restaurant.management.publisher;

import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.restaurant.application.port.out.message.publisher.PaymentRequestPublisher;
import com.poly.restaurant.management.message.PaymentRequestMessage;
import com.poly.restaurant.management.message.PaymentRequestMessageAvro;
import com.poly.restaurant.management.mapper.PaymentMessageMapper;
import com.poly.restaurant.management.config.RestaurantConfigTopicData;
import com.poly.restaurant.management.helper.PaymentMessageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Kafka Publisher chịu trách nhiệm gửi yêu cầu thanh toán từ Restaurant Service
 * đến Payment Service thông qua Kafka topic.
 * 
 * NGHIỆP VỤ:
 * - Gửi payment request message đến payment service
 * - Đảm bảo reliable message delivery
 * - Xử lý message serialization từ domain model sang Avro format
 * 
 * PATTERNS ÁP DỤNG:
 * - Publisher Pattern: Tách biệt message publishing logic
 * - Message Mapping: Chuyển đổi domain message sang Avro format
 * - Error Handling: Xử lý các exception khi gửi message
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRequestKafka implements PaymentRequestPublisher {

    private final KafkaProducer<String, PaymentRequestMessageAvro> kafkaProducer;
    private final PaymentMessageMapper paymentMessageMapper;
    private final RestaurantConfigTopicData restaurantConfigTopicData;

    /**
     * Gửi payment request message đến Kafka topic
     * 
     * @param paymentRequestMessage Message chứa thông tin thanh toán
     */
    @Override
    public void publish(PaymentRequestMessage paymentRequestMessage) {
        // Validate input parameters
        validateInputParameters(paymentRequestMessage);
        
        // Log thông tin bắt đầu xử lý
        logProcessingStart(paymentRequestMessage);
        
        try {
            // Chuyển đổi domain message sang Avro format
            PaymentRequestMessageAvro paymentRequestMessageAvro = 
                    paymentMessageMapper.paymentRequestMessageToPaymentRequestMessageAvro(paymentRequestMessage);
            
            // Gửi message đến Kafka topic
            sendMessageToKafka(paymentRequestMessageAvro, paymentRequestMessage);
            
            // Log thành công
            logProcessingSuccess(paymentRequestMessage);
            
        } catch (Exception e) {
            // Log và xử lý lỗi
            handleProcessingError(paymentRequestMessage, e);
            throw new RuntimeException("Failed to publish payment request message", e);
        }
    }

    /**
     * Validate các tham số đầu vào
     */
    private void validateInputParameters(PaymentRequestMessage paymentRequestMessage) {
        if (!PaymentMessageHelper.isValidPaymentRequestMessage(paymentRequestMessage)) {
            throw new IllegalArgumentException("Invalid PaymentRequestMessage");
        }
    }

    /**
     * Gửi message đến Kafka topic
     */
    private void sendMessageToKafka(PaymentRequestMessageAvro paymentRequestMessageAvro, 
                                   PaymentRequestMessage paymentRequestMessage) {
        String topicName = restaurantConfigTopicData.getRestaurantPaymentRequestTopicName();
        String messageKey = paymentRequestMessage.getOrderId();
        
        kafkaProducer.send(topicName, messageKey, paymentRequestMessageAvro, 
                (result, ex) -> {
                    if (ex == null) {
                        log.debug("Payment request message sent successfully to Kafka topic: {} with key: {}", 
                                topicName, messageKey);
                    } else {
                        log.error("Failed to send payment request message to Kafka topic: {} with key: {}. Error: {}", 
                                topicName, messageKey, ex.getMessage(), ex);
                    }
                });
    }

    /**
     * Log thông tin bắt đầu xử lý
     */
    private void logProcessingStart(PaymentRequestMessage paymentRequestMessage) {
        log.info("Bắt đầu gửi payment request message cho order id: {}", 
                paymentRequestMessage.getOrderId());
    }

    /**
     * Log thông tin xử lý thành công
     */
    private void logProcessingSuccess(PaymentRequestMessage paymentRequestMessage) {
        log.info("Payment request message đã được gửi thành công cho order id: {}", 
                paymentRequestMessage.getOrderId());
    }

    /**
     * Xử lý lỗi khi gửi message
     */
    private void handleProcessingError(PaymentRequestMessage paymentRequestMessage, Exception e) {
        log.error("Lỗi khi gửi payment request message cho order id: {}. Error: {}", 
                paymentRequestMessage.getOrderId(), e.getMessage(), e);
    }
}
