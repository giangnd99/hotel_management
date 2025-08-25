package com.poly.booking.management.domain.saga.cancellation;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingCancelledEvent;
import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCancellationStep implements SagaStep<NotificationMessageResponse> {

    private final BookingCancellationSagaHelper bookingCancellationSagaHelper;

    @Override
    @Transactional
    public void process(NotificationMessageResponse data) {
        log.info("Processing booking cancellation for saga id: {}", data.getSagaId());


        Booking domainEvent = bookingCancellationSagaHelper.executeBookingCancellation(data);


        log.info("Booking cancellation completed successfully for booking: {}",
                domainEvent.getId().getValue());
    }

    @Override
    @Transactional
    public void rollback(NotificationMessageResponse data) {
        log.info("Rolling back booking cancellation for saga id: {}", data.getSagaId());

//        RoomOutboxMessage outboxMessage = bookingCancellationSagaHelper.validateAndGetOutboxMessageForRollback(data);
//        if (outboxMessage == null) {
//            return;
//        }

//        bookingCancellationSagaHelper.executeRollbackBusinessLogic(data);

//        bookingCancellationSagaHelper.updateOutboxMessageForRollback(outboxMessage, data);

        log.info("Booking cancellation rollback completed for booking: {}", data.getBookingId());
    }
}
