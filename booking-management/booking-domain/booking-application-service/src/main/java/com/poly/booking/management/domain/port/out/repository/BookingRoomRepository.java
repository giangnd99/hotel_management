package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.entity.BookingRoom;

import java.util.Optional;
import java.util.UUID;

public interface BookingRoomRepository {

    Optional<BookingRoom> save(BookingRoom bookingRoom);

    Optional<BookingRoom> findById(UUID bookingRoomId);

    Optional<BookingRoom> findByBookingIdAndRoomId(UUID bookingId, UUID roomId);
}
