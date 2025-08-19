package com.poly.payment.management.message.publisher;

import com.poly.booking.management.domain.kafka.model.BookingPaymentPendingResponseAvro;
import com.poly.booking.management.domain.kafka.model.BookingPaymentRequestAvro;

import com.poly.kafka.producer.AbstractKafkaPublisher;
import com.poly.kafka.producer.service.KafkaProducer;

import com.poly.payment.management.domain.message.BookingPaymentPendingResponse;
import com.poly.payment.management.domain.message.BookingPaymentRequest;

import com.poly.payment.management.message.mapper.PaymentKafkaDataMapper;

import org.springframework.stereotype.Component;

@Component
public class BookingPaymentPendingKafkaPublisher  extends AbstractKafkaPublisher<String, BookingPaymentPendingResponseAvro, BookingPaymentPendingResponse> {

    private final PaymentKafkaDataMapper paymentKafkaDataMapper;

    protected BookingPaymentPendingKafkaPublisher(KafkaProducer<String, BookingPaymentPendingResponseAvro> kafkaProducer, PaymentKafkaDataMapper paymentKafkaDataMapper) {
        super(kafkaProducer);
        this.paymentKafkaDataMapper = paymentKafkaDataMapper;
    }


    @Override
    protected String getTopicName() {
        return "payment-pending-response-topic";
    }

    @Override
    protected String getKey(BookingPaymentPendingResponse message) {
        return message.getSagaId();
    }

    @Override
    protected BookingPaymentPendingResponseAvro toAvro(BookingPaymentPendingResponse message) {
        return paymentKafkaDataMapper.toBookingPaymentPendingResponseAvro(message);
    }

    @Override
    protected String getMessageName() {
        return "PaymentPending";
    }
}
