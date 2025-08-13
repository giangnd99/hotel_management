package com.poly.restaurant.management.publisher;

import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.restaurant.application.port.out.message.publisher.PaymentRequestPublisher;
import com.poly.restaurant.management.message.PaymentRequestMessage;
import com.poly.restaurant.management.message.PaymentRequestMessageAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRequestKafka implements PaymentRequestPublisher {

    private final KafkaProducer<String, PaymentRequestMessageAvro> kafkaProducer;

    @Override
    public void publish(PaymentRequestMessage paymentRequestMessage) {
        kafkaProducer.send();
    }
}
