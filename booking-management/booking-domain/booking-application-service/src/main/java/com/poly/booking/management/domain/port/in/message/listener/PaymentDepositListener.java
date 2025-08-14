package com.poly.booking.management.domain.port.in.message.listener;


import com.poly.booking.management.messaging.message.PaymentMessageResponse;

public interface PaymentDepositListener {

    void paymentDepositCompleted(PaymentMessageResponse paymentDepositResponseMessageResponse);

    void paymentDepositCancelled(PaymentMessageResponse paymentDepositResponseMessageResponse);
}
