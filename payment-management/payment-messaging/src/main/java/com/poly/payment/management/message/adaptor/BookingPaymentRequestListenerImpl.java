package com.poly.payment.management.message.adaptor;

import com.poly.payment.management.domain.port.input.BookingPaymentRequestListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingPaymentRequestListenerImpl implements BookingPaymentRequestListener {

    

    @Override
    public void handleBookingPaymentRequest() {

    }
}
