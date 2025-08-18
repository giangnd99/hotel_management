package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.valueobject.DateCustom;

public class BookingConfirmedEvent extends BookingEvent {
    public BookingConfirmedEvent(Booking booking, DateCustom createdAt) {
        super(booking, createdAt);
    }
}
