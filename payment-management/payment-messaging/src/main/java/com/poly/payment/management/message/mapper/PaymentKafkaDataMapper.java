package com.poly.payment.management.message.mapper;

import com.poly.booking.management.domain.kafka.model.BookingPaymentPendingResponseAvro;
import com.poly.booking.management.domain.kafka.model.BookingPaymentRequestAvro;
import com.poly.booking.management.domain.kafka.model.BookingPaymentResponseAvro;
import com.poly.booking.management.domain.kafka.model.PaymentBookingStatus;

import com.poly.payment.management.domain.message.BookingPaymentPendingResponse;
import com.poly.payment.management.domain.message.BookingPaymentRequest;
import com.poly.payment.management.domain.message.BookingPaymentResponse;
import com.poly.payment.management.domain.message.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentKafkaDataMapper {

    public BookingPaymentPendingResponseAvro toBookingPaymentPendingResponseAvro(BookingPaymentPendingResponse message) {
        return BookingPaymentPendingResponseAvro.newBuilder()
                .setBookingId(message.getBookingId())
                .setCustomerId(message.getCustomerId())
                .setPaymentBookingStatus(PaymentBookingStatus.valueOf(message.getPaymentBookingStatus().name()))
                .setCreatedAt(message.getCreatedAt())
                .setId(message.getId())
                .setSagaId(message.getSagaId())
                .setPrice(message.getPrice())
                .setCreatedAt(message.getCreatedAt())
                .setUrlPayment(message.getUrlPayment())
                .build();
    }

    public BookingPaymentRequestAvro toBookingPaymentRequestAvro(BookingPaymentRequest message) {
        return BookingPaymentRequestAvro.newBuilder()
                .setBookingId(message.getBookingId())
                .setCustomerId(message.getCustomerId())
                .setPaymentBookingStatus(PaymentBookingStatus.valueOf(message.getPaymentBookingStatus().name()))
                .setCreatedAt(message.getCreatedAt())
                .setId(message.getId())
                .setSagaId(message.getSagaId())
                .setPrice(message.getPrice())
                .setCreatedAt(message.getCreatedAt())
                .build();
    }

    public BookingPaymentRequest toDomainMessage(BookingPaymentRequestAvro avro){
        return BookingPaymentRequest.builder()
                .bookingId(avro.getBookingId())
                .sagaId(avro.getSagaId())
                .createdAt(avro.getCreatedAt())
                .customerId(avro.getCustomerId())
                .id(avro.getId())
                .paymentBookingStatus(com.poly.payment.management.domain.message.PaymentBookingStatus.valueOf(avro.getPaymentBookingStatus().name()))
                .price(avro.getPrice())
                .build();
    }

    public BookingPaymentResponse toDomainMessage(BookingPaymentResponseAvro avro){
        return BookingPaymentResponse.builder()
                .bookingId(avro.getBookingId())
                .paymentId(avro.getPaymentId())
                .price(avro.getPrice())
                .failureMessages(avro.getFailureMessages())
                .id(avro.getId())
                .sagaId(avro.getSagaId())
                .paymentStatus(PaymentStatus.valueOf(avro.getPaymentStatus().name()))
                .createdAt(avro.getCreatedAt())
                .customerId(avro.getCustomerId())
                .sagaId(avro.getSagaId())
                .build();
    }

    public BookingPaymentResponseAvro toBookingPaymentResponseAvro(BookingPaymentResponse message){
        return BookingPaymentResponseAvro.newBuilder()
                .setBookingId(message.getBookingId())
                .setPaymentId(message.getPaymentId())
                .setPrice(message.getPrice())
                .setFailureMessages(message.getFailureMessages())
                .setId(message.getId())
                .setSagaId(message.getSagaId())
                .setPaymentStatus(com.poly.booking.management.domain.kafka.model.PaymentStatus.valueOf(message.getPaymentStatus().name()))
                .setCreatedAt(message.getCreatedAt())
                .setCustomerId(message.getCustomerId())
                .build();
    }
}
