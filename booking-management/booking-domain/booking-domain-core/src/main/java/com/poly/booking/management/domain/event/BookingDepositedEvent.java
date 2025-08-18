package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.valueobject.DateCustom;

public class BookingDepositedEvent extends BookingEvent {
    public BookingDepositedEvent(Booking booking, DateCustom createdAt) {
        super(booking, createdAt);
    }
}
