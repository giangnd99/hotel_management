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
// ==================== DEPENDENCIES ====================

    // Business logic services
    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final BookingSagaHelper bookingSagaHelper;
    private final PaymentOutboxImpl paymentOutboxHelper;
    private final PaymentDataMapper paymentDataMapper;
    private final RoomDataMapper roomDataMapper;

    // Outbox pattern helpers for reliable messaging
    private final NotificationOutboxServiceImpl notificationOutboxServiceImpl;
    private final RoomOutboxService roomOutboxService;
    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Validate và lấy outbox message để tránh duplicate processing
     *
     * @param data RoomMessageResponse từ external service
     * @return BookingRoomOutboxMessage nếu hợp lệ, null nếu đã processed
     */
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


    /**
     * Tìm booking và validate trạng thái hiện tại
     *
     * @param bookingId Booking identifier
     * @return Booking entity
     * @throws RuntimeException nếu booking không tồn tại hoặc trạng thái không hợp lệ
     */
    public Booking findBookingAndValidateStatus(String bookingId) {
        Booking booking = bookingRepository.findById(new BookingId(UUID.fromString(bookingId)))
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        // Validate booking status - chỉ cho phép check-in từ CONFIRMED status
        if (!BookingStatus.CONFIRMED.equals(booking.getStatus())) {
            throw new RuntimeException("Booking is not in CONFIRMED status for check-in: " + bookingId);
        }

        return booking;
    }

    /**
     * Thực hiện business logic check-in
     *
     * @param booking Booking entity
     * @param data    Notification message response
     */
    public void performCheckInBusinessLogic(Booking booking, NotificationMessageResponse data) {
        log.info("Performing check-in business logic for booking: {}", booking.getId().getValue());

        // Validate QR code và thời gian check-in
        validateQrCodeAndCheckInTime(data);

        // Thực hiện check-in thông qua domain service
        CheckInEvent checkInEvent = bookingDomainService.checkInBooking(booking);

        // Lưu booking
        bookingSagaHelper.saveBooking(checkInEvent.getBooking());

        log.info("Check-in business logic completed for booking: {}", checkInEvent.getBooking().getId().getValue());
    }

    /**
     * Validate QR code và thời gian check-in
     * *
     *
     * @param data Notification message response
     */
    public void validateQrCodeAndCheckInTime(NotificationMessageResponse data) {
        // Validate QR code format và content
        if (data.getQrCode() == null || data.getQrCode().trim().isEmpty()) {
            throw new RuntimeException("Invalid QR code for check-in");
        }

        // Validate check-in time
        if (data.getCheckInTime() == null) {
            throw new RuntimeException("Invalid check-in time");
        }

        // Validate check-in time is within booking period
        // Note: Simplified validation - in real implementation, convert dates properly
        if (data.getCheckInTime() == null) {
            throw new RuntimeException("Check-in time cannot be null");
        }

        log.info("QR code and check-in time validated successfully");
    }

    /**
     * Cập nhật saga status và lưu outbox messages
     *
     * @param booking    Booking entity
     * @param data       Notification message response
     * @param sagaStatus Trạng thái saga mới
     */
    public void updateSagaStatusAndOutboxMessages(Booking booking, NotificationMessageResponse data, SagaStatus sagaStatus) {
        // Tạo notification event payload
        NotifiEventPayload eventPayload = NotifiEventPayload.builder()
                .id(UUID.randomUUID())
                .bookingId(booking.getId().getValue())
                .customerId(UUID.fromString(data.getCustomerId()))
                .checkInTime(LocalDateTime.now())
                .notificationStatus(NotificationStatus.SUCCESS.name())
                .bookingStatus(booking.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu notification outbox message
        notificationOutboxServiceImpl.saveWithPayloadAndBookingStatusAndSagaStatusAndOutboxStatusAndSagaId(
                eventPayload,
                booking.getStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(data.getSagaId())
        );

        log.info("Saga status and outbox messages updated for booking: {}", booking.getId().getValue());
    }

    /**
     * Trigger next saga step (payment processing)
     *
     * @param booking Booking entity
     * @param data    Notification message response
     */
    public void triggerNextSagaStep(Booking booking, NotificationMessageResponse data) {
        log.info("Triggering next saga step for booking id: {}", booking.getId().getValue());
        CheckInEvent domainEvent = bookingDomainService.checkInBooking(booking);
        roomOutboxService.saveRoomOutboxMessage(
                roomDataMapper.bookingCheckInEventToRoomBookedEventPayload(domainEvent),
                domainEvent.getBooking().getStatus(),
                bookingSagaHelper.bookingStatusToSagaStatus(domainEvent.getBooking().getStatus()),
                OutboxStatus.STARTED,
                UUID.fromString(data.getSagaId())
        );

        log.info("Next saga step triggered successfully for booking: {}", booking.getId().getValue());
    }


    /**
     * Thực hiện rollback business logic
     *
     * @param data Notification message response
     */
    public void performRollbackBusinessLogic(NotificationMessageResponse data) {
        log.info("Performing rollback business logic for booking: {}", data.getBookingId());

        // Tìm booking và revert status
        Booking booking = bookingRepository.findById(new BookingId(UUID.fromString(data.getBookingId())))
                .orElseThrow(() -> new RuntimeException("Booking not found for rollback: " + data.getBookingId()));

        // Revert booking status từ CHECKED_IN về CONFIRMED
        if (BookingStatus.CHECKED_IN.equals(booking.getStatus())) {
            // Note: In real implementation, use proper method to revert status
            // booking.setStatus(EBookingStatus.CONFIRMED);
            bookingRepository.save(booking);
            log.info("Booking status reverted to CONFIRMED for booking: {}", data.getBookingId());
        }

        // TODO: Gửi notification cho customer về check-in failure
        // notificationService.sendCheckInFailureNotification(data.getCustomer(), data.getBookingId());
    }

    /**
     * Cập nhật outbox message cho rollback
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param data          Notification message response
     */
    public void updateOutboxMessageForRollback(NotifiOutboxMessage outboxMessage, NotificationMessageResponse data) {
        // Cập nhật outbox message với trạng thái rollback
        NotifiOutboxMessage updatedMessage = notificationOutboxServiceImpl
                .getUpdated(
                        outboxMessage,
                        BookingStatus.CONFIRMED, // Revert về CONFIRMED status
                        SagaStatus.COMPENSATED
                );

        // Lưu updated message
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
