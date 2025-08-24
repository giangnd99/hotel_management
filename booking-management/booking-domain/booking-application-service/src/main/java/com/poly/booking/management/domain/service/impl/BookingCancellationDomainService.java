package com.poly.booking.management.domain.service.impl;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingCancelledEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.port.out.message.publisher.NotificationRequestMessagePublisher;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.DateCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookingCancellationDomainService {

    private static final int CANCELLATION_DEADLINE_DAYS = 1;
    private final BookingRepository bookingRepository;
    private final NotificationRequestMessagePublisher notificationRequestMessagePublisher;


    public Booking cancelBooking(UUID bookingId, String cancellationReason) {
        log.info("Processing cancellation for booking: {} with reason: {}",
                bookingId, cancellationReason);

        Booking bookingFounded = validateBookingCanBeCancelled(bookingId);

        boolean isRefundable = determineRefundability(bookingFounded);

        if (isAfterCheckIn(bookingFounded)) {
            isRefundable = false;
        }

        if (isRefundable) {

        }

        bookingFounded.cancelBooking();

        log.info("Booking cancelled successfully: {}. Refundable: {}. Reason: {}",
                bookingFounded.getId().getValue(), isRefundable, cancellationReason);

        return bookingFounded;
    }


    private Booking validateBookingCanBeCancelled(UUID bookingId) {
        if (bookingId == null) {
            throw new BookingDomainException("Booking cannot be null for cancellation");
        }

        Booking bookingFounded = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BookingDomainException("Not found Booking"));

        BookingStatus currentStatus = bookingFounded.getStatus();

        if (currentStatus == BookingStatus.CANCELLED) {
            throw new BookingDomainException("Booking is already cancelled: " + bookingFounded.getId().getValue());
        }

        log.debug("Booking validation passed for cancellation: {}", bookingFounded.getId().getValue());

        return bookingFounded;
    }


    private boolean determineRefundability(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            log.warn("Check-in date is null for booking: {}. Assuming refundable.", booking.getId().getValue());
            return true;
        }

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime checkInLocalDate = checkInDate.getValue();

        long daysUntilCheckIn = ChronoUnit.DAYS.between(today, checkInLocalDate);

        boolean isRefundable = daysUntilCheckIn > CANCELLATION_DEADLINE_DAYS;

        log.info("Cancellation refundability for booking {}: {} days until check-in, refundable: {}",
                booking.getId().getValue(), daysUntilCheckIn, isRefundable);

        return isRefundable;
    }

    private boolean isAfterCheckIn(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInLocalDate = checkInDate.getValue().toLocalDate();

        return today.isAfter(checkInLocalDate);
    }

    public long getDaysUntilCheckIn(Booking booking) {
        DateCustom checkInDate = booking.getCheckInDate();
        if (checkInDate == null) {
            return -1;
        }

        LocalDate today = LocalDate.now();
        LocalDate checkInLocalDate = checkInDate.getValue().toLocalDate();

        return ChronoUnit.DAYS.between(today, checkInLocalDate);
    }

}
