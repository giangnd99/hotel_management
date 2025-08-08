package com.poly.booking.management.domain.saga.payment;

import com.poly.booking.management.domain.dto.message.PaymentMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingDepositedEvent;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.mapper.PaymentDataMapper;
import com.poly.booking.management.domain.outbox.model.payment.BookingPaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.saga.SagaStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCheckInPaymentSaga implements SagaStep<PaymentMessageResponse> {

    private final PaymentDataMapper paymentDataMapper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final BookingSagaHelper bookingSagaHelper;
    private final BookingRepository bookingRepository;
    private final BookingDomainService bookingDomainService;


    @Transactional
    @Override
    public void process(PaymentMessageResponse paymentResponse) {
        log.info("Processing checkin payment for saga id: {}", paymentResponse.getSagaId());

        // Step 1: Validate outbox message to prevent duplicate processing
        BookingPaymentOutboxMessage outboxMessage = validateAndGetPaymentOutboxMessage(paymentResponse);
        if (outboxMessage == null) {
            return; // Already processed
        }

        // Step 2: Execute business logic - complete payment
        BookingPaidEvent domainEvent = executePaymentCompletion(paymentResponse);

        // Step 3: Update saga status and save payment outbox message
        updatePaymentOutboxMessage(outboxMessage, domainEvent);

        // Step 4: Trigger end step - paid booking
        triggerEndStep(domainEvent, paymentResponse.getSagaId());

        log.info("Payment processing completed successfully for booking: {}",
                domainEvent.getBooking().getId().getValue());
    }


    @Override
    public void rollback(PaymentMessageResponse data) {

    }


    private BookingPaymentOutboxMessage validateAndGetPaymentOutboxMessage(PaymentMessageResponse paymentResponse) {
        Optional<BookingPaymentOutboxMessage> outboxMessageOpt =
                paymentOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(paymentResponse.getSagaId()),
                        SagaStatus.PROCESSING);
        if (outboxMessageOpt.isEmpty()) {
            log.error("Could not find payment outbox message for saga id: {}", paymentResponse.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }

    private BookingPaidEvent executePaymentCompletion(PaymentMessageResponse paymentResponse) {

        log.info("Executing payment completion for booking: {}", paymentResponse.getBookingId());
        return completePaymentForBooking(paymentResponse);
    }

    private BookingPaidEvent completePaymentForBooking(PaymentMessageResponse paymentResponse) {
        log.info("Completing payment for booking with id: {}", paymentResponse.getBookingId());
        Booking booking = bookingSagaHelper.findBooking(paymentResponse.getBookingId());
        BookingPaidEvent domainEvent = bookingDomainService.payBooking(booking);
        bookingRepository.save(booking);
        return domainEvent;
    }

    private void triggerEndStep(BookingPaidEvent domainEvent, String sagaId) {
    }

    private void updatePaymentOutboxMessage(BookingPaymentOutboxMessage outboxMessage, BookingPaidEvent domainEvent) {
    }


}
