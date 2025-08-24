package com.poly.booking.management.domain.port.in.message.listener;


import com.poly.booking.management.domain.message.reponse.PaymentMessageResponse;

public interface PaymentListener {

    void paymentCompleted(PaymentMessageResponse paymentDepositResponseMessageResponse);

    void paymentCancelled(PaymentMessageResponse paymentDepositResponseMessageResponse);
}
