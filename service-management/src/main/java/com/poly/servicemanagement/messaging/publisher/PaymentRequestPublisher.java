package com.poly.servicemanagement.messaging.publisher;

import com.poly.servicemanagement.messaging.message.PaymentRequestMessage;

public interface PaymentRequestPublisher {
    void publish(PaymentRequestMessage paymentRequestMessage);
}
