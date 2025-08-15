package com.poly.restaurant.application.port.in.message.listener;

import com.poly.message.model.payment.PaymentResponseMessage;


public interface PaymentResponseListener {

    void onPaymentSuccess(PaymentResponseMessage paymentResponseMessage);

    void onPaymentFailure(PaymentResponseMessage paymentResponseMessage);
}
