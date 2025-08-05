package com.poly.booking.management.domain.saga.impl;

import org.springframework.stereotype.Service;

import com.poly.booking.management.domain.dto.message.PaymentMessageResponse;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.port.in.message.listener.payment.PaymentDepositListener;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.saga.payment.BookingPaymentSaga;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentDepositListenerImpl implements PaymentDepositListener {


    private final BookingPaymentSaga bookingPaymentSaga;
            
    @Override
    public void paymentDepositCompleted(PaymentMessageResponse paymentDepositResponseMessageResponse) {
        bookingPaymentSaga.process(paymentDepositResponseMessageResponse);
        log.info("Payment deposit completed for booking: {}", paymentDepositResponseMessageResponse.getBookingId());

    }

    @Override
    public void paymentDepositCancelled(PaymentMessageResponse paymentDepositResponseMessageResponse) {
        bookingPaymentSaga.rollback(paymentDepositResponseMessageResponse);
        log.info("Payment deposit cancelled for booking: {}", paymentDepositResponseMessageResponse.getBookingId());
        
    }

}
