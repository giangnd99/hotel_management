package com.poly.booking.management.domain.saga.notification;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.CheckInEvent;
import com.poly.booking.management.domain.mapper.PaymentDataMapper;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.booking.management.domain.outbox.service.RoomOutboxService;
import com.poly.booking.management.domain.outbox.service.impl.NotificationOutboxServiceImpl;
import com.poly.booking.management.domain.outbox.service.impl.PaymentOutboxImpl;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.NotificationStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSagaHelper {

    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final BookingSagaHelper bookingSagaHelper;
    private final RoomDataMapper roomDataMapper;

    private final NotificationOutboxServiceImpl notificationOutboxServiceImpl;
    private final RoomOutboxService roomOutboxService;


    public NotifiOutboxMessage validateAndGetOutboxMessage(NotificationMessageResponse data) {
        Optional<NotifiOutboxMessage> outboxMessageOpt =
                notificationOutboxServiceImpl.getBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);

        if (outboxMessageOpt.isEmpty()) {
            log.info("Outbox message with saga id: {} already processed!", data.getSagaId());
            return null;
        }

        return outboxMessageOpt.get();
    }



    public Booking findBookingAndValidateStatus(String bookingId) {
        Booking booking = bookingRepository.findById(UUID.fromString(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        if (!BookingStatus.CONFIRMED.equals(booking.getStatus())) {
            throw new RuntimeException("Booking is not in CONFIRMED status for check-in: " + bookingId);
        }

        return booking;
    }

    public void performCheckInBusinessLogic(Booking booking, NotificationMessageResponse data) {
        log.info("Performing check-in business logic for booking: {}", booking.getId().getValue());

        validateQrCodeAndCheckInTime(data);

        CheckInEvent checkInEvent = bookingDomainService.checkInBooking(booking);

        bookingSagaHelper.saveBooking(checkInEvent.getBooking());

        log.info("Check-in business logic completed for booking: {}", checkInEvent.getBooking().getId().getValue());
    }


    public void validateQrCodeAndCheckInTime(NotificationMessageResponse data) {

        if (data.getQrCode() == null || data.getQrCode().trim().isEmpty()) {
            throw new RuntimeException("Invalid QR code for check-in");
        }

        if (data.getCheckInTime() == null) {
            throw new RuntimeException("Invalid check-in time");
        }

        if (data.getCheckInTime() == null) {
            throw new RuntimeException("Check-in time cannot be null");
        }

        log.info("QR code and check-in time validated successfully");
    }


    public void triggerNextSagaStep(Booking booking, NotificationMessageResponse data) {
        log.info("Triggering next saga step for booking id: {}", booking.getId().getValue());
        CheckInEvent domainEvent = bookingDomainService.checkInBooking(booking);
        roomOutboxService.saveRoomOutboxMessage(
                roomDataMapper.bookingCheckInEventToRoomBookedEventPayload(domainEvent.getBooking()),
                domainEvent.getBooking().getStatus(),
                bookingSagaHelper.bookingStatusToSagaStatus(domainEvent.getBooking().getStatus()),
                OutboxStatus.STARTED,
                UUID.fromString(data.getSagaId())
        );

        log.info("Next saga step triggered successfully for booking: {}", booking.getId().getValue());
    }


    public void performRollbackBusinessLogic(NotificationMessageResponse data) {
        log.info("Performing rollback business logic for booking: {}", data.getBookingId());

        Booking booking = bookingRepository.findById(UUID.fromString(data.getBookingId()))
                .orElseThrow(() -> new RuntimeException("Booking not found for rollback: " + data.getBookingId()));

        if (BookingStatus.CHECKED_IN.equals(booking.getStatus())) {

            bookingRepository.save(booking);
            log.info("Booking status reverted to CONFIRMED for booking: {}", data.getBookingId());
        }

    }

    /**
     * Cập nhật outbox message cho rollback
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param data          Notification message response
     */
    public void updateOutboxMessageForRollback(NotifiOutboxMessage outboxMessage, NotificationMessageResponse data) {
        NotifiOutboxMessage updatedMessage = notificationOutboxServiceImpl
                .getUpdated(
                        outboxMessage,
                        BookingStatus.CONFIRMED, // Revert về CONFIRMED status
                        SagaStatus.COMPENSATED
                );

        notificationOutboxServiceImpl.save(updatedMessage);

        log.info("Outbox message updated for rollback with saga: {}", data.getSagaId());
    }

    public NotifiOutboxMessage validateAndGetOutboxMessageForRollback(NotificationMessageResponse data) {
        Optional<NotifiOutboxMessage> outboxMessageOpt =
                notificationOutboxServiceImpl.getBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.COMPENSATED);
        if (outboxMessageOpt.isEmpty()) {
            log.info("Outbox message with saga id: {} already processed!", data.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }
}
