package com.poly.booking.management.domain.saga.payment;

import com.poly.booking.management.domain.dto.message.PaymentMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingPaidEvent;
import com.poly.booking.management.domain.exception.BookingDomainException;
import com.poly.booking.management.domain.mapper.PaymentDataMapper;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.PaymentOutboxService;
import com.poly.booking.management.domain.outbox.service.impl.RoomOutboxServiceImpl;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.saga.SagaStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckOutStep implements SagaStep<PaymentMessageResponse> {

    private final CheckoutSagaHelper checkoutSagaHelper;


    @Transactional
    @Override
    public void process(PaymentMessageResponse paymentResponse) {
        log.info("Processing checkin payment for saga id: {}", paymentResponse.getSagaId());

        // Step 1: Validate outbox message to prevent duplicate processing
        PaymentOutboxMessage outboxMessage = checkoutSagaHelper.validateAndGetPaymentOutboxMessage(paymentResponse);
        if (outboxMessage == null) {
            return; // Already processed
        }

        // Step 2: Execute business logic - complete payment
        BookingPaidEvent domainEvent = checkoutSagaHelper.executePaymentCompletion(paymentResponse);

        // Step 3: Update saga status and save payment outbox message
        checkoutSagaHelper.updatePaymentOutboxMessage(outboxMessage, domainEvent);

        // Step 4: Trigger end step - paid booking
        checkoutSagaHelper.triggerEndStep(outboxMessage, domainEvent, SagaStatus.valueOf(outboxMessage.getSagaStatus().name()));

        log.info("Payment processing completed successfully for booking: {}",
                domainEvent.getBooking().getId().getValue());
    }


    @Override
    public void rollback(PaymentMessageResponse paymentResponse) {
        log.info("Rolling back payment for saga id: {}", paymentResponse.getSagaId());

        // Step 1: Find payment outbox message for rollback
        PaymentOutboxMessage outboxMessage = checkoutSagaHelper.findPaymentOutboxMessageForRollback(paymentResponse);
        if (outboxMessage == null) {
            return; // Already rolled back
        }

        // Step 2: Execute rollback business logic
        Booking booking = checkoutSagaHelper.rollbackToCheckOut(paymentResponse);

        // Step 3: Update payment outbox message with rollback status
        checkoutSagaHelper.updatePaymentOutboxMessageForRollback(outboxMessage, booking);

        log.info("Payment rollback completed successfully for booking: {}",
                booking.getId().getValue());

    }


}
