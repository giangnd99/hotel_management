package com.poly.restaurant.application.port.in.message.listener;

import com.poly.restaurant.management.message.PaymentResponseMessage;

public interface PaymentResponseListener {

    void onPaymentSuccess(PaymentResponseMessage paymentResponseMessage);

    void onPaymentFailure(PaymentResponseMessage paymentResponseMessage);
}
