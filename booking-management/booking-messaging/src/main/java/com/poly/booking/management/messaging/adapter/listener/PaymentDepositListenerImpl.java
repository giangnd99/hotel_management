package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.port.in.message.listener.PaymentDepositListener;
import com.poly.booking.management.domain.message.reponse.PaymentMessageResponse;
import org.springframework.stereotype.Service;

import com.poly.booking.management.domain.saga.payment.DepositStep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentDepositListenerImpl implements PaymentDepositListener {


    private final DepositStep depositStep;
            
    @Override
    public void paymentDepositCompleted(PaymentMessageResponse paymentDepositResponseMessageResponse) {
        depositStep.process(paymentDepositResponseMessageResponse);
        log.info("Payment deposit completed for booking: {}", paymentDepositResponseMessageResponse.getBookingId());

    }

    @Override
    public void paymentDepositCancelled(PaymentMessageResponse paymentDepositResponseMessageResponse) {
        depositStep.rollback(paymentDepositResponseMessageResponse);
        log.info("Payment deposit cancelled for booking: {}", paymentDepositResponseMessageResponse.getBookingId());
        
    }

}
