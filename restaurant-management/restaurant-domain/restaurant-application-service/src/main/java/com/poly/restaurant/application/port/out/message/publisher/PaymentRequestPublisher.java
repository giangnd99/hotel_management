package com.poly.restaurant.application.port.out.message.publisher;

import com.poly.restaurant.management.message.PaymentRequestMessage;

public interface PaymentRequestPublisher {

    void publish(PaymentRequestMessage paymentRequestMessage);
}
