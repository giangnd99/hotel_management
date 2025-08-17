package com.poly.booking.management.messaging.adapter.listener;

import com.poly.booking.management.domain.port.in.message.listener.PaymentListener;
import com.poly.booking.management.domain.saga.payment.CheckOutStep;
import com.poly.booking.management.domain.message.PaymentMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentListenerImpl implements PaymentListener {

    private final CheckOutStep checkoutStep;

    @Override
    public void paymentCompleted(PaymentMessageResponse paymentDepositResponseMessageResponse) {
        checkoutStep.process(paymentDepositResponseMessageResponse);
        log.info("Payment completed for booking: {}", paymentDepositResponseMessageResponse.getBookingId());
    }

    @Override
    public void paymentCancelled(PaymentMessageResponse paymentDepositResponseMessageResponse) {
        checkoutStep.rollback(paymentDepositResponseMessageResponse);
    }
}
