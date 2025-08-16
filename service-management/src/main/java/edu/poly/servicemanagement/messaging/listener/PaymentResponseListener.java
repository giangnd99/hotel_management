package edu.poly.servicemanagement.messaging.listener;

import edu.poly.servicemanagement.messaging.message.PaymentResponseMessage;

public interface PaymentResponseListener {
    void paymentCompleted(PaymentResponseMessage paymentResponseMessage);
    void paymentCancelled(PaymentResponseMessage paymentResponseMessage);
    void paymentFailed(PaymentResponseMessage paymentResponseMessage);
}
