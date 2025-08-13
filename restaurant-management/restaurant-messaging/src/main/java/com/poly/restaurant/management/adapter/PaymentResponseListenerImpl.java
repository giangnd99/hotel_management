package com.poly.restaurant.management.adapter;

import com.poly.restaurant.application.port.in.message.listener.PaymentResponseListener;
import com.poly.restaurant.management.message.PaymentResponseMessage;
import org.springframework.stereotype.Component;

@Component
public class PaymentResponseListenerImpl implements PaymentResponseListener {


    @Override
    public void onPaymentSuccess(PaymentResponseMessage paymentResponseMessage) {
        //log the payment success
        // change status of the order to PAID
        //log the order status change Paid with order id
        // save data to database
    }

    @Override
    public void onPaymentFailure(PaymentResponseMessage paymentResponseMessage) {
        //log the payment failure
        // change status of the order to CANCELLED
        // log the order status change Cancelled with order id
        // save data to database
    }
}
