package com.poly.booking.management.domain.event;

public class BookingCreatedEvent extends BookingEvent{
    public BookingCreatedEvent(Object source, com.poly.booking.management.domain.entity.Booking booking, com.poly.domain.valueobject.DateCustom createdAt) {
        super(booking, createdAt);
    }
}
