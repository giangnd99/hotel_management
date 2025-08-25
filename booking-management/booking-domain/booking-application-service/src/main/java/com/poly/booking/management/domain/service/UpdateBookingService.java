package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.dto.request.UpdateBookingRequest;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Booking;

import java.util.UUID;

public interface UpdateBookingService {

    BookingCreatedResponse updateBooking(UUID bookingId, UpdateBookingRequest booking);
}
