package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.valueobject.BookingId;

import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);

    Optional<Booking> findById(BookingId bookingId);
}
