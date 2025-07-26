package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.valueobject.DateCustom;

public class BookingCancelledEvent extends BookingEvent {
    public BookingCancelledEvent(Booking booking, DateCustom createdAt) {
        super(booking, createdAt);
    }
}
