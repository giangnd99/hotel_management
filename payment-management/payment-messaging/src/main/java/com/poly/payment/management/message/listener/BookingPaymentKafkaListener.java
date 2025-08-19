package com.poly.payment.management.message.listener;

import com.poly.booking.management.domain.kafka.model.BookingPaymentRequestAvro;
import com.poly.kafka.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingPaymentKafkaListener implements KafkaConsumer<BookingPaymentRequestAvro> {



    @Override
    public void receive(List<BookingPaymentRequestAvro> messages, List<String> keys, List<Integer> partitions, List<Long> offsets) {

    }
}
