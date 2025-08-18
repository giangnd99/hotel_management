package com.poly.room.management.domain.port.out.publisher.request;

import com.poly.message.model.booking.BookingRequestMessage;

public interface BookingCheckoutRequestPublisher {
    void publish(BookingRequestMessage bookingRequestMessage);
}
