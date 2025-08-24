package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.dto.request.UpdateBookingRequest;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.service.UpdateBookingService;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.DateCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateBookingServiceImpl implements UpdateBookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking updateBooking(UUID bookingId, UpdateBookingRequest request) {

        if (bookingId == null) {
            log.error("Booking id is null!");
            throw new IllegalArgumentException("Booking id is null!");
        }
        Booking bookingFounded = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking with id: " + bookingId + " could not be found!"));
        if (bookingFounded.getCheckInDate().getValue() == request.getCheckInDate()) {
            log.error("Check in date is not changed!");
            throw new IllegalArgumentException("Check in date is not changed!");
        }

        if (Objects.equals(request.getNumberOfGuests(), bookingFounded.getNumberOfGuests())) {
            log.error("Number of guests is not changed!");
            throw new IllegalArgumentException("Number of guests is not changed!");
        }
        if (Objects.equals(request.getCheckOutDate(), bookingFounded.getCheckOutDate().getValue())) {
            log.error("Check out date is not changed!");
            throw new IllegalArgumentException("Check out date is not changed!");
        }
        bookingFounded.setNumberOfGuests(request.getNumberOfGuests());
        bookingFounded.setCheckInDate(DateCustom.of(request.getCheckInDate()));
        bookingFounded.setCheckOutDate(DateCustom.of(request.getCheckOutDate()));
        return bookingRepository.save(bookingFounded);
    }
}
