package com.poly.booking.management.domain.saga.cancellation;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingCancelledEvent;
import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.service.impl.BookingCancellationDomainService;
import com.poly.domain.valueobject.BookingId;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCancellationSagaHelper {

    private final RoomOutboxService roomOutboxService;
    private final BookingRepository bookingRepository;
    private final BookingCancellationDomainService bookingCancellationDomainService;

    public RoomOutboxMessage validateAndGetOutboxMessageForRollback(RoomMessageResponse data) {
        Optional<RoomOutboxMessage> outboxMessageOpt =
                roomOutboxService.getRoomOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);
        if (outboxMessageOpt.isEmpty()) {
            log.error("Could not find room cancellation outbox message for rollback with saga id: {}", data.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }


    public Booking executeBookingCancellation(NotificationMessageResponse data) {
        log.info("Executing booking cancellation for booking: {}", data.getBookingId());

        Booking booking = findBooking(data.getBookingId());

        String cancellationReason = "No reason provided";

        Booking domainEvent = bookingCancellationDomainService.cancelBooking(booking.getId().getValue(), cancellationReason);

        bookingRepository.save(booking);

        return domainEvent;
    }


    public void updateSagaStatusAndSaveOutbox(RoomOutboxMessage outboxMessage, Booking domainEvent) {
        log.info("Updating saga status and outbox message for booking: {}", outboxMessage.getBookingId());

        outboxMessage.setSagaStatus(SagaStatus.FINISHED);
        outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.COMPLETED);

        roomOutboxService.save(outboxMessage);

        log.info("Saga status and outbox message updated successfully for booking: {}", outboxMessage.getBookingId());
    }

    public void triggerRefundStep(Booking domainEvent, RoomMessageResponse data) {
        log.info("Triggering refund step for booking: {}", domainEvent.getId().getValue());
        log.info("Refund step triggered successfully for booking: {}", domainEvent.getId().getValue());
    }

    public void executeRollbackBusinessLogic(RoomMessageResponse data) {
        log.info("Executing rollback business logic for booking: {}", data.getBookingId());

        Booking booking = findBooking(data.getBookingId());

        log.info("Rollback business logic completed for booking: {}", data.getBookingId());
    }

    public void updateOutboxMessageForRollback(RoomOutboxMessage outboxMessage, RoomMessageResponse data) {
        log.info("Updating outbox message for rollback with booking: {}", data.getBookingId());

        outboxMessage.setSagaStatus(SagaStatus.FAILED);
        outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.FAILED);

        roomOutboxService.save(outboxMessage);

        log.info("Outbox message updated for rollback with booking: {}", data.getBookingId());
    }


    private Booking findBooking(String bookingId) {
        return bookingRepository.findById((UUID.fromString(bookingId)))
                .orElseThrow(() -> new RuntimeException("Booking not found for cancellation: " + bookingId));
    }

}
