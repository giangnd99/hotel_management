package com.poly.booking.management.domain.event;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.event.DomainEvent;
import com.poly.domain.valueobject.DateCustom;

public abstract class BookingEvent implements DomainEvent<Booking> {

    private final Booking booking;
    private final DateCustom createdAt;

    public BookingEvent(Booking booking, DateCustom createdAt) {
        this.booking = booking;
        this.createdAt = createdAt;
    }

    public Booking getBooking() {
        return booking;
    }

    public DateCustom getCreatedAt() {
        return createdAt;
    }
}
