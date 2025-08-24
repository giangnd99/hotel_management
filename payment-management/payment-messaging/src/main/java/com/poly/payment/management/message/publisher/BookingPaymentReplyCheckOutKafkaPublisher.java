package com.poly.payment.management.message.publisher;

import com.poly.booking.management.domain.kafka.model.BookingPaymentResponseAvro;
import com.poly.kafka.producer.AbstractKafkaPublisher;
import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.payment.management.domain.message.BookingPaymentResponse;
import com.poly.payment.management.domain.port.output.publisher.BookingPaymentCheckoutReplyPublisher;
import com.poly.payment.management.domain.port.output.publisher.BookingPaymentReplyPublisher;
import com.poly.payment.management.message.mapper.PaymentKafkaDataMapper;
import org.springframework.stereotype.Component;


@Component
public class BookingPaymentReplyCheckOutKafkaPublisher
        extends AbstractKafkaPublisher<String, BookingPaymentResponseAvro, BookingPaymentResponse>
        implements BookingPaymentCheckoutReplyPublisher {

    private final PaymentKafkaDataMapper paymentKafkaDataMapper;

    protected BookingPaymentReplyCheckOutKafkaPublisher(KafkaProducer<String, BookingPaymentResponseAvro> kafkaProducer, PaymentKafkaDataMapper paymentKafkaDataMapper) {
        super(kafkaProducer);
        this.paymentKafkaDataMapper = paymentKafkaDataMapper;
    }

    @Override
    protected String getTopicName() {
        return "booking-payment-checkout-response";
    }

    @Override
    protected String getKey(BookingPaymentResponse message) {
        return message.getSagaId();
    }

    @Override
    protected BookingPaymentResponseAvro toAvro(BookingPaymentResponse message) {
        return paymentKafkaDataMapper.toBookingPaymentResponseAvro(message);
    }


    @Override
    protected String getMessageName() {
        return "BookingPaymentCheckOutReply";
    }

}
