package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.entity.*;
import com.poly.booking.management.domain.event.*;
import com.poly.domain.valueobject.CustomerId;

import java.util.List;

public interface BookingDomainService {

    BookingCreatedEvent validateAndInitiateBooking(Booking booking,
                                                   Order order,
                                                   List<Service> service,
                                                   List<Room> rooms);

    DepositCreatedEvent validateAndInitiateDeposit(Deposit deposit, Booking booking);

    DepositPaidEvent payDeposit(Deposit deposit, Booking booking);

    DepositRefundedEvent refundDeposit(Deposit deposit, Booking booking);

    DepositCancelledEvent cancelPaymentDeposit(Deposit deposit, Booking booking);

    BookingCancelledEvent cancelDepositBooking(Booking booking);

    CheckInEvent checkInBooking(Booking booking);

    void updateBooking(Booking booking);

    BookingPaidEvent payBooking(Booking booking);

    CheckOutEvent checkOutBooking(Booking booking);

    void cancelBooking(Booking booking);

}
