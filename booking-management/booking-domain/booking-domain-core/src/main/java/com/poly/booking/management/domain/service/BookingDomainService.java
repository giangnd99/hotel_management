package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.entity.*;
import com.poly.booking.management.domain.event.*;

import java.util.List;

public interface BookingDomainService {

    BookingCreatedEvent validateAndInitiateBooking(Booking booking,
                                                   List<Room> rooms);

    BookingCancelledEvent cancelDepositBooking(Booking booking);

    BookingDepositedEvent depositBooking(Booking booking);

    BookingConfirmedEvent confirmBooking(Booking booking);

    CheckInEvent checkInBooking(Booking booking);

    void updateBooking(Booking booking);

    BookingPaidEvent payBooking(Booking booking);

    CheckOutEvent checkOutBooking(Booking booking);

    BookingCancelledEvent cancelBooking(Booking booking);

}
