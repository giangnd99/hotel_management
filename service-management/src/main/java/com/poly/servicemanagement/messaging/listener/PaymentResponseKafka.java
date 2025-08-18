package com.poly.servicemanagement.messaging.listener;

import com.poly.servicemanagement.messaging.message.PaymentResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseKafka {

    private final PaymentResponseListener paymentResponseListener;

    @KafkaListener(topics = "${kafka.topic.service-payment-response}", groupId = "${kafka.group-id}")
    public void receive(@Payload List<PaymentResponseMessage> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        
        log.info("Received {} payment response messages", messages.size());
        
        for (PaymentResponseMessage message : messages) {
            try {
                processPaymentResponse(message);
            } catch (Exception e) {
                log.error("Error processing payment response for order: {}", message.getOrderNumber(), e);
            }
        }
    }

    private void processPaymentResponse(PaymentResponseMessage message) {
        log.info("Processing payment response for order: {} with status: {}", 
                message.getOrderNumber(), message.getStatus());
        
        switch (message.getStatus().toUpperCase()) {
            case "COMPLETED":
                paymentResponseListener.paymentCompleted(message);
                break;
            case "CANCELLED":
                paymentResponseListener.paymentCancelled(message);
                break;
            case "FAILED":
                paymentResponseListener.paymentFailed(message);
                break;
            default:
                log.warn("Unknown payment status: {} for order: {}", message.getStatus(), message.getOrderNumber());
        }
    }
}
