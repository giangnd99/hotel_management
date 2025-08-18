package com.poly.servicemanagement.messaging.listener;

import com.poly.servicemanagement.messaging.message.PaymentResponseMessage;

public interface PaymentResponseListener {
    void paymentCompleted(PaymentResponseMessage paymentResponseMessage);
    void paymentCancelled(PaymentResponseMessage paymentResponseMessage);
    void paymentFailed(PaymentResponseMessage paymentResponseMessage);
}
