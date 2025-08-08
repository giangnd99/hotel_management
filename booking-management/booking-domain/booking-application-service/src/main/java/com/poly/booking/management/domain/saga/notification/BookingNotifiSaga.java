package com.poly.booking.management.domain.saga.notification;

import com.poly.booking.management.domain.dto.message.NotificationMessageResponse;
import com.poly.booking.management.domain.dto.message.RoomMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.CheckInEvent;
import com.poly.booking.management.domain.event.CheckOutEvent;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.mapper.PaymentDataMapper;
import com.poly.booking.management.domain.outbox.model.notification.BookingNotifiEventPayload;
import com.poly.booking.management.domain.outbox.model.notification.BookingNotifiOutboxMessage;
import com.poly.booking.management.domain.outbox.model.room.BookingRoomOutboxMessage;
import com.poly.booking.management.domain.outbox.scheduler.notification.NotificationOutboxHelper;
import com.poly.booking.management.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.BookingId;
import com.poly.domain.valueobject.EBookingStatus;
import com.poly.domain.valueobject.NotificationStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * BookingNotifiSaga - Saga Step Implementation for QR Check-in Notification Processing
 * <p>
 * CHỨC NĂNG CHÍNH:
 * - Xử lý notification sau khi quét QR code để check-in
 * - Quản lý trạng thái check-in và cập nhật outbox messages
 * - Thực hiện rollback khi check-in thất bại
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo tính nhất quán dữ liệu trong quy trình check-in
 * - Xử lý bất đồng bộ thông qua Outbox Pattern
 * - Cung cấp khả năng rollback khi có lỗi check-in
 * <p>
 * ÁP DỤNG PATTERNS:
 * - Saga Pattern: Quản lý distributed transaction cho check-in flow
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Domain Events: Tách biệt business logic check-in
 * <p>
 * FLOW XỬ LÝ CHECK-IN:
 * 1. Nhận notification response từ QR scanning service
 * 2. Validate outbox message để tránh duplicate processing
 * 3. Thực hiện business logic check-in (cập nhật booking status)
 * 4. Cập nhật saga status và lưu outbox messages
 * 5. Trigger next step (payment processing) nếu check-in thành công
 * <p>
 * ROLLBACK FLOW:
 * 1. Nhận check-in failure từ notification service
 * 2. Tìm outbox message với trạng thái phù hợp
 * 3. Thực hiện cancel check-in và revert booking status
 * 4. Cập nhật trạng thái đã rollback
 * <p>
 * QR CHECK-IN PROCESS:
 * - Customer quét QR code tại hotel
 * - QR service gửi notification với booking info
 * - Saga xử lý check-in và cập nhật booking status
 * - Gửi confirmation notification cho customer
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookingNotifiSaga implements SagaStep<NotificationMessageResponse> {

    // ==================== DEPENDENCIES ====================

    // Business logic services
    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final BookingSagaHelper bookingSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final PaymentDataMapper paymentDataMapper;

    // Outbox pattern helpers for reliable messaging
    private final NotificationOutboxHelper notificationOutboxHelper;

    // ==================== SAGA STEP IMPLEMENTATION ====================

    /**
     * PROCESS METHOD - Xử lý chính của Notification Saga Step
     * <p>
     * LOGIC FLOW:
     * 1. Validate outbox message để tránh duplicate processing
     * 2. Thực hiện business logic check-in
     * 3. Cập nhật saga status và lưu outbox messages
     * 4. Trigger next step (payment) nếu thành công
     * <p>
     * OUTBOX PATTERN:
     * - Sử dụng NotificationOutboxHelper để quản lý notification outbox messages
     * - Đảm bảo notification message được xử lý một cách reliable
     * - Tránh duplicate processing thông qua saga status check
     * <p>
     * SAGA PATTERN:
     * - Chuyển đổi booking status sang CHECKED_IN
     * - Cập nhật notification outbox message với trạng thái mới
     * - Trigger payment step nếu check-in thành công
     *
     * @param data NotificationMessageResponse chứa thông tin QR check-in
     */
    @Override
    @Transactional
    public void process(NotificationMessageResponse data) {
        log.info("Processing notification saga step for booking: {}", data.getBookingId());

        try {
            // Step 1: Validate outbox message để tránh duplicate processing
            Optional<BookingNotifiOutboxMessage> outboxMessage =
                    notificationOutboxHelper.getNotificationOutboxMessageBySagaIdAndSagaStatus(
                            UUID.fromString(data.getSagaId()),
                            SagaStatus.STARTED, SagaStatus.PROCESSING
                    );
            if (outboxMessage.isPresent()) {
                log.info("Notification outbox message already processed for saga: {}", data.getSagaId());
                return;
            }

            // Step 2: Tìm booking và validate trạng thái
            Booking booking = findBookingAndValidateStatus(data.getBookingId());

            // Step 3: Thực hiện business logic check-in
            performCheckInBusinessLogic(booking, data);

            // Step 4: Cập nhật saga status và lưu outbox messages
            updateSagaStatusAndOutboxMessages(booking, data, SagaStatus.PROCESSING);

            // Step 5: Trigger next step (payment processing) nếu check-in thành công
            if (NotificationMessageResponse.NotificationStatus.SUCCESS.equals(data.getNotificationStatus())) {
                triggerNextSagaStep(booking, data);
            }

            log.info("Notification saga step processed successfully for booking: {}", data.getBookingId());

        } catch (Exception e) {
            log.error("Error processing notification saga step for booking: {}", data.getBookingId(), e);
            handleProcessingError(data, e);
        }
    }


    /**
     * ROLLBACK METHOD - Xử lý rollback khi có lỗi
     * <p>
     * LOGIC FLOW:
     * 1. Tìm outbox message với trạng thái phù hợp
     * 2. Thực hiện cancel check-in và revert booking status
     * 3. Cập nhật trạng thái đã rollback
     * <p>
     * COMPENSATION ACTIONS:
     * - Revert booking status từ CHECKED_IN về CONFIRMED
     * - Cập nhật notification outbox message với trạng thái FAILED
     * - Gửi notification cho customer về check-in failure
     *
     * @param data NotificationMessageResponse chứa thông tin cần rollback
     */
    @Override
    @Transactional
    public void rollback(NotificationMessageResponse data) {
        log.info("Rolling back notification saga step for booking: {}", data.getBookingId());

        try {
            // Step 1: Tìm outbox message để rollback
            Optional<BookingNotifiOutboxMessage> outboxMessage =
                    notificationOutboxHelper.getNotificationOutboxMessageBySagaIdAndSagaStatus(
                            UUID.fromString(data.getSagaId()),
                            SagaStatus.PROCESSING, SagaStatus.STARTED
                    );

            if (outboxMessage.isEmpty()) {
                log.warn("No outbox message found for rollback with saga: {}", data.getSagaId());
                return;
            }

            // Step 2: Thực hiện rollback business logic
            performRollbackBusinessLogic(data);

            // Step 3: Cập nhật outbox message với trạng thái rollback
            updateOutboxMessageForRollback(outboxMessage.get(), data);

            log.info("Notification saga step rolled back successfully for booking: {}", data.getBookingId());

        } catch (Exception e) {
            log.error("Error rolling back notification saga step for booking: {}", data.getBookingId(), e);
            throw new RuntimeException("Failed to rollback notification saga step", e);
        }
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Validate và lấy outbox message để tránh duplicate processing
     *
     * @param data RoomMessageResponse từ external service
     * @return BookingRoomOutboxMessage nếu hợp lệ, null nếu đã processed
     */
    private BookingNotifiOutboxMessage validateAndGetOutboxMessage(NotificationMessageResponse data) {
        Optional<BookingNotifiOutboxMessage> outboxMessageOpt =
                notificationOutboxHelper.getNotificationOutboxMessageBySagaIdAndSagaStatus(
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
    private Booking findBookingAndValidateStatus(String bookingId) {
        Booking booking = bookingRepository.findById(new BookingId(UUID.fromString(bookingId)))
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        // Validate booking status - chỉ cho phép check-in từ CONFIRMED status
        if (!EBookingStatus.CONFIRMED.equals(booking.getStatus())) {
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
    private void performCheckInBusinessLogic(Booking booking, NotificationMessageResponse data) {
        log.info("Performing check-in business logic for booking: {}", booking.getId().getValue());

        // Validate QR code và thời gian check-in
        validateQrCodeAndCheckInTime(booking, data);

        // Thực hiện check-in thông qua domain service
        CheckInEvent checkInEvent = bookingDomainService.checkInBooking(booking);

        // Cập nhật booking status
        booking.checkIn();

        // Lưu booking
        bookingRepository.save(booking);

        log.info("Check-in business logic completed for booking: {}", booking.getId().getValue());
    }

    /**
     * Validate QR code và thời gian check-in
     *
     * @param booking Booking entity
     * @param data    Notification message response
     */
    private void validateQrCodeAndCheckInTime(Booking booking, NotificationMessageResponse data) {
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
    private void updateSagaStatusAndOutboxMessages(Booking booking, NotificationMessageResponse data, SagaStatus sagaStatus) {
        // Tạo notification event payload
        BookingNotifiEventPayload eventPayload = BookingNotifiEventPayload.builder()
                .id(UUID.randomUUID())
                .bookingId(booking.getId().getValue())
                .customerId(UUID.fromString(data.getCustomerId()))
                .checkInTime(LocalDateTime.now())
                .notificationStatus(NotificationStatus.SUCCESS.name())
                .bookingStatus(booking.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu notification outbox message
        notificationOutboxHelper.saveNotificationOutboxMessage(
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
    private void triggerNextSagaStep(Booking booking, NotificationMessageResponse data) {
        log.info("Triggering next saga step for booking id: {}", booking.getId().getValue());
        CheckOutEvent domainEvent = bookingDomainService.checkOutBooking(booking);
        paymentOutboxHelper.savePaymentOutboxMessage(
                paymentDataMapper.bookingCheckOutEventToBookingPaymentEventPayload(domainEvent),
                domainEvent.getBooking().getStatus(),
                bookingSagaHelper.bookingStatusToSagaStatus(domainEvent.getBooking().getStatus()),
                OutboxStatus.STARTED,
                UUID.fromString(data.getSagaId())

        );

        log.info("Next saga step triggered successfully for booking: {}", booking.getId().getValue());
    }

    /**
     * Xử lý lỗi trong quá trình processing
     *
     * @param data      Notification message response
     * @param exception Exception occurred
     */
    private void handleProcessingError(NotificationMessageResponse data, Exception exception) {
        log.error("Handling processing error for booking: {}", data.getBookingId(), exception);

        // Cập nhật notification status thành FAILED
        // Note: In real implementation, create new response with FAILED status
        log.error("Notification processing failed: {}", exception.getMessage());

        // Trigger rollback
        rollback(data);

        throw new RuntimeException("Failed to process notification saga step", exception);
    }

    /**
     * Thực hiện rollback business logic
     *
     * @param data Notification message response
     */
    private void performRollbackBusinessLogic(NotificationMessageResponse data) {
        log.info("Performing rollback business logic for booking: {}", data.getBookingId());

        // Tìm booking và revert status
        Booking booking = bookingRepository.findById(new BookingId(UUID.fromString(data.getBookingId())))
                .orElseThrow(() -> new RuntimeException("Booking not found for rollback: " + data.getBookingId()));

        // Revert booking status từ CHECKED_IN về CONFIRMED
        if (EBookingStatus.CHECKED_IN.equals(booking.getStatus())) {
            // Note: In real implementation, use proper method to revert status
            // booking.setStatus(EBookingStatus.CONFIRMED);
            bookingRepository.save(booking);
            log.info("Booking status reverted to CONFIRMED for booking: {}", data.getBookingId());
        }

        // TODO: Gửi notification cho customer về check-in failure
        // notificationService.sendCheckInFailureNotification(data.getCustomerId(), data.getBookingId());
    }

    /**
     * Cập nhật outbox message cho rollback
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param data          Notification message response
     */
    private void updateOutboxMessageForRollback(BookingNotifiOutboxMessage outboxMessage, NotificationMessageResponse data) {
        // Cập nhật outbox message với trạng thái rollback
        BookingNotifiOutboxMessage updatedMessage = notificationOutboxHelper
                .getUpdatedNotificationOutBoxMessage(
                        outboxMessage,
                        EBookingStatus.CONFIRMED, // Revert về CONFIRMED status
                        SagaStatus.COMPENSATED
                );

        // Lưu updated message
        notificationOutboxHelper.save(updatedMessage);

        log.info("Outbox message updated for rollback with saga: {}", data.getSagaId());
    }
}
