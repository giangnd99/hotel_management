package com.poly.restaurant.management.publisher;

import com.poly.config.KafkaTopicsConfig;
import com.poly.message.model.payment.PaymentRequestMessage;
import com.poly.producer.KafkaJsonProducer;
import com.poly.producer.callback.KafkaProducerCallback;
import com.poly.restaurant.application.port.out.message.publisher.PaymentRequestPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentPublisherKafka implements PaymentRequestPublisher {

    private final KafkaJsonProducer<String, PaymentRequestMessage> kafkaJsonProducer;

    public void publishPaymentRequest(PaymentRequestMessage message) {
        log.info("Publishing direct payment request for order {} to topic {}", message.getCorrelationId(), KafkaTopicsConfig.PAYMENT_REQUEST_TOPIC);

        var callback = new KafkaProducerCallback<>(
                KafkaTopicsConfig.PAYMENT_REQUEST_TOPIC,
                message.getCorrelationId(),
                message
        );
        // Gửi message
        kafkaJsonProducer.send(
                KafkaTopicsConfig.PAYMENT_REQUEST_TOPIC,
                message.getCorrelationId(),
                message,
                callback
        );
    }

    @Override
    public void publish(PaymentRequestMessage paymentRequestMessage) {
        paymentRequestMessage.setSourceService("restaurant-management");
        publishPaymentRequest(paymentRequestMessage);
        log.info("Payment request published for order with id: {}", paymentRequestMessage.getCorrelationId());
    }
}
