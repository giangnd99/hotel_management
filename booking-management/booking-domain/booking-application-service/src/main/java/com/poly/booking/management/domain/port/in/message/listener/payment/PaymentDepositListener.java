package com.poly.booking.management.domain.port.in.message.listener.payment;

import com.poly.booking.management.domain.dto.message.PaymentMessageResponse;

public interface PaymentDepositListener {

    void paymentDepositCompleted(PaymentMessageResponse paymentDepositResponseMessageResponse);

    void paymentDepositCancelled(PaymentMessageResponse paymentDepositResponseMessageResponse);
}
