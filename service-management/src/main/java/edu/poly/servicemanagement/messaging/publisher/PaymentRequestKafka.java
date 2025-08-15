package edu.poly.servicemanagement.messaging.publisher;

import edu.poly.servicemanagement.messaging.message.PaymentRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRequestKafka implements PaymentRequestPublisher {

    private final KafkaTemplate<String, PaymentRequestMessage> kafkaTemplate;

    @Override
    public void publish(PaymentRequestMessage paymentRequestMessage) {
        log.info("Publishing payment request message for order: {}", 
                paymentRequestMessage.getOrderNumber());
        
        String key = paymentRequestMessage.getOrderNumber();
        
        CompletableFuture<SendResult<String, PaymentRequestMessage>> future = 
                kafkaTemplate.send("service-payment-request", key, paymentRequestMessage);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Payment request message sent successfully for order: {} to partition: {} with offset: {}", 
                        paymentRequestMessage.getOrderNumber(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send payment request message for order: {}", 
                        paymentRequestMessage.getOrderNumber(), ex);
            }
        });
    }
}
