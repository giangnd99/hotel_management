package com.poly.payment.management.domain.port.input;


import com.poly.payment.management.domain.message.BookingPaymentRequest;

public interface BookingPaymentRequestListener {

    void handleBookingPaymentRequest(BookingPaymentRequest bookingPaymentRequest);
}
