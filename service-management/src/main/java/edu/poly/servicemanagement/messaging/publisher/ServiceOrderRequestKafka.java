package edu.poly.servicemanagement.messaging.publisher;

import edu.poly.servicemanagement.messaging.message.ServiceOrderRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceOrderRequestKafka implements ServiceOrderRequestPublisher {

    private final KafkaTemplate<String, ServiceOrderRequestMessage> kafkaTemplate;

    @Override
    public void publish(ServiceOrderRequestMessage serviceOrderRequestMessage) {
        log.info("Publishing service order request message for order: {}", 
                serviceOrderRequestMessage.getOrderNumber());
        
        String key = serviceOrderRequestMessage.getOrderNumber();
        
        CompletableFuture<SendResult<String, ServiceOrderRequestMessage>> future = 
                kafkaTemplate.send("service-order-request", key, serviceOrderRequestMessage);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Service order request message sent successfully for order: {} to partition: {} with offset: {}", 
                        serviceOrderRequestMessage.getOrderNumber(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send service order request message for order: {}", 
                        serviceOrderRequestMessage.getOrderNumber(), ex);
            }
        });
    }
}
