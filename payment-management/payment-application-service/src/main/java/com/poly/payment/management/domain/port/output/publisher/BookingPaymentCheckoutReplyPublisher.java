package com.poly.payment.management.domain.port.output.publisher;

import com.poly.payment.management.domain.message.BookingPaymentResponse;

public interface BookingPaymentCheckoutReplyPublisher {

    void publish(BookingPaymentResponse response);
}
