package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.valueobject.DateCustom;

public class BookingPaidEvent extends BookingEvent {
    public BookingPaidEvent(Booking booking, DateCustom createdAt) {
        super(booking, createdAt);
    }
}
