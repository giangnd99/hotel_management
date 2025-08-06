package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.entity.*;
import com.poly.booking.management.domain.event.*;

import java.util.List;

public interface BookingDomainService {

    BookingCreatedEvent validateAndInitiateBooking(Booking booking,
                                                   List<Room> rooms);

    DepositCreatedEvent validateAndInitiateDeposit(Deposit deposit, Booking booking);

    DepositPaidEvent payDeposit(Deposit deposit, Booking booking);

    DepositRefundedEvent refundDeposit(Deposit deposit, Booking booking,String reason);

    DepositCancelledEvent cancelPaymentDeposit(Deposit deposit, Booking booking,List<String> failureMessages);

    BookingCancelledEvent cancelDepositBooking(Booking booking);

    BookingDepositEvent confirmDepositBooking(Booking booking);

    CheckInEvent checkInBooking(Booking booking);

    void updateBooking(Booking booking);

    BookingPaidEvent payBooking(Booking booking);

    CheckOutEvent checkOutBooking(Booking booking);

    BookingCancelledEvent cancelBooking(Booking booking);

}
