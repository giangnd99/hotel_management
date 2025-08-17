package edu.poly.servicemanagement.messaging.publisher;

import edu.poly.servicemanagement.messaging.message.PaymentRequestMessage;

public interface PaymentRequestPublisher {
    void publish(PaymentRequestMessage paymentRequestMessage);
}
