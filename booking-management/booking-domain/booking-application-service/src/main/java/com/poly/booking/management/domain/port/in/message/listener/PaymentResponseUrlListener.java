package com.poly.booking.management.domain.port.in.message.listener;


import com.poly.booking.management.domain.message.reponse.BookingPaymentPendingResponse;

public interface PaymentResponseUrlListener {

    void paymentResponseUrlReceived(BookingPaymentPendingResponse paymentResponseUrl);
}
