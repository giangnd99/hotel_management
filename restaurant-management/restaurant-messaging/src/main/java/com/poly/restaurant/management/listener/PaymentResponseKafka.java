package com.poly.restaurant.management.listener;

import com.poly.kafka.consumer.KafkaConsumer;
import com.poly.restaurant.application.port.in.message.listener.PaymentResponseListener;
import com.poly.restaurant.management.mapper.PaymentMessageMapper;
import com.poly.restaurant.management.message.PaymentResponseMessageAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResponseKafka implements KafkaConsumer<PaymentResponseMessageAvro> {

    private final PaymentMessageMapper paymentMessageMapper;
    private final PaymentResponseListener paymentResponseListener;

    @Override
    @KafkaListener(topics = "${kafka.topic.payment-response}", id = "${kafka.group-id}")
    public void receive(List<PaymentResponseMessageAvro> messages, List<String> keys, List<Integer> partitions, List<Long> offsets) {
//        // Validate input parameters
//        validateInputParameters(messages, keys, partitions, offsets);
//
//        // Log thông tin batch processing
//        logBatchProcessingInfo(messages.size(), keys, partitions, offsets);
//
//        // Process each customer creation message
//        processCustomerCreationMessages(messages);
    }
}
