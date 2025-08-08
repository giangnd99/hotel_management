package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.valueobject.DateCustom;

public class BookingDepositEvent extends BookingEvent {
    public BookingDepositEvent(Booking booking, DateCustom createdAt) {
        super(booking, createdAt);
    }
}
