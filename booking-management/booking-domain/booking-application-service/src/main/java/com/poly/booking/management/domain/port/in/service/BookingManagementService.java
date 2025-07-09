package com.poly.booking.management.domain.port.in.service;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.dto.CreateBookingCommand;
import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.booking.management.domain.dto.RoomSearchQuery;
import com.poly.domain.valueobject.BookingStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingManagementService {

    BookingDto createBooking(CreateBookingCommand command);

    List<RoomDto> searchAvailableRooms(RoomSearchQuery query);

    Optional<Object> getBookingById(UUID bookingId);

    List<BookingDto> getAllBookings();

    List<BookingDto> getBookingsByCustomerId(Long customerId);

    BookingDto cancelBooking(UUID bookingId);

    BookingDto updateBookingDetails(UUID bookingId, BookingStatus newStatus, String specialRequests, Integer numberOfGuests);
}
