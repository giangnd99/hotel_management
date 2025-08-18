package com.poly.room.management.domain.port.out.publisher.request;

import com.poly.message.model.payment.PaymentRequestMessage;

public interface PaymentCheckOutRequestPublisher {

    void publish(PaymentRequestMessage paymentRequestMessage);
}
