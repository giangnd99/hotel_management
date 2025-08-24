package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.dto.request.UpdateBookingRequest;
import com.poly.booking.management.domain.entity.Booking;

import java.util.UUID;

public interface UpdateBookingService {

    Booking updateBooking(UUID bookingId, UpdateBookingRequest booking);
}
