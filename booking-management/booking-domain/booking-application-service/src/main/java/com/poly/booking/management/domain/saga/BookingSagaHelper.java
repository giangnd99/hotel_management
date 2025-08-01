package com.poly.booking.management.domain.saga;

import com.poly.booking.management.domain.dto.message.payment.PaymentMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingSagaHelper {

    private final BookingRepository bookingRepository;
    private final BookingDomainService bookingDomainService;

    public Booking findBooking(String bookingId) {
        Optional<Booking> orderResponse = bookingRepository.findById(new BookingId(UUID.fromString(bookingId)));
        if (orderResponse.isEmpty()) {
            log.error("Booking with id: {} could not be found!", bookingId);
            throw new BookingDomainException("Booking with id " + bookingId + " could not be found!");
        }
        return orderResponse.get();
    }

    public SagaStatus bookingStatusToSagaStatus(EBookingStatus status) {
        return switch (status) {
            case PAID -> SagaStatus.PROCESSING;
            case CONFIRMED -> SagaStatus.SUCCEEDED;
            case CANCELLED -> SagaStatus.COMPENSATED;
            case CANCELLING -> SagaStatus.COMPENSATING;
            default -> SagaStatus.STARTED;
        };
    }

    void saveBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    public BookingPaidEvent completePaymentForBooking(PaymentMessageResponse data) {
        log.info("Completing payment for booking with id: {}", data.getBookingId());
        Booking booking = findBooking(data.getBookingId());
        BookingPaidEvent domainEvent = bookingDomainService.payBooking(booking);
        bookingRepository.save(booking);
        return domainEvent;
    }
}
