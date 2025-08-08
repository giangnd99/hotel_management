package com.poly.booking.management.domain.saga.room;

import com.poly.booking.management.domain.dto.message.RoomMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingConfirmedEvent;
import com.poly.booking.management.domain.event.BookingDepositedEvent;
import com.poly.booking.management.domain.mapper.BookingDataMapper;
import com.poly.booking.management.domain.mapper.NotificationDataMapper;
import com.poly.booking.management.domain.outbox.model.room.BookingRoomOutboxMessage;
import com.poly.booking.management.domain.outbox.scheduler.notification.NotificationOutboxHelper;
import com.poly.booking.management.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.poly.booking.management.domain.outbox.scheduler.room.RoomOutboxHelper;
import com.poly.booking.management.domain.port.out.repository.BookingRepository;
import com.poly.booking.management.domain.saga.BookingSagaHelper;
import com.poly.booking.management.domain.service.BookingDomainService;
import com.poly.domain.valueobject.ReservationStatus;
import com.poly.outbox.OutboxStatus;
import com.poly.saga.SagaStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * BookingRoomSaga - Saga Step Implementation for Room Reservation Processing
 * <p>
 * CHỨC NĂNG:
 * - Xử lý việc đặt phòng trong quy trình Saga của hệ thống booking
 * - Quản lý trạng thái đặt phòng và cập nhật outbox messages
 * - Thực hiện rollback khi có lỗi xảy ra
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo tính nhất quán dữ liệu trong quy trình đặt phòng
 * - Xử lý bất đồng bộ thông qua Outbox Pattern
 * - Cung cấp khả năng rollback khi có lỗi
 * <p>
 * ÁP DỤNG PATTERNS:
 * - Saga Pattern: Quản lý distributed transaction
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Domain Events: Tách biệt business logic
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookingRoomSaga implements SagaStep<RoomMessageResponse> {

    // Dependencies for data mapping and business logic
    private final NotificationDataMapper notificationDataMapper;
    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;

    // Saga orchestration helpers
    private final BookingSagaHelper bookingSagaHelper;

    // Outbox pattern helpers for reliable messaging
    private final RoomOutboxHelper roomOutboxHelper;
    private final NotificationOutboxHelper notificationOutboxHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;

    /**
     * PROCESS METHOD - Xử lý chính của Saga Step
     * <p>
     * LOGIC:
     * 1. Kiểm tra outbox message để tránh duplicate processing
     * 2. Thực hiện business logic đặt phòng
     * 3. Cập nhật trạng thái Saga và lưu outbox message
     * <p>
     * OUTBOX PATTERN:
     * - Sử dụng RoomOutboxHelper để quản lý outbox messages
     * - Đảm bảo message được gửi một cách reliable
     * - Tránh duplicate processing thông qua saga status check
     * <p>
     * SAGA PATTERN:
     * - Chuyển đổi booking status sang saga status
     * - Cập nhật outbox message với trạng thái mới
     */
    @Override
    public void process(RoomMessageResponse data) {
        log.info("Processing room reservation for saga id: {}", data.getSagaId());

        // Step 1: Validate outbox message to prevent duplicate processing
        BookingRoomOutboxMessage outboxMessage = validateAndGetOutboxMessage(data);
        if (outboxMessage == null) {
            return; // Already processed
        }

        // Step 2: Execute business logic - reserve room
        BookingConfirmedEvent domainEvent = executeRoomReservation(outboxMessage);

        // Step 3: Update saga status and save outbox message
        updateSagaStatusAndSaveOutbox(outboxMessage, domainEvent);

        // Step 4: Trigger next step - send QR code for check-ind reservation
        triggerSendQrCodeStep(domainEvent, data);

        log.info("Room reservation completed successfully for booking: {}",
                domainEvent.getBooking().getId().getValue());
    }

    /**
     * ROLLBACK METHOD - Xử lý rollback khi có lỗi
     * <p>
     * LOGIC:
     * 1. Tìm outbox message với trạng thái phù hợp
     * 2. Thực hiện cancel booking
     * 3. Lưu trạng thái đã rollback
     * <p>
     * SAGA COMPENSATION:
     * - Thực hiện compensation action khi có lỗi
     * - Đảm bảo tính nhất quán dữ liệu
     */
    @Override
    public void rollback(RoomMessageResponse data) {
        log.info("Rolling back room reservation for saga id: {}", data.getSagaId());

        // Step 1: Find outbox message for rollback
        BookingRoomOutboxMessage outboxMessage = findOutboxMessageForRollback(data);
        if (outboxMessage == null) {
            return; // Already rolled back
        }

        // Step 2: Execute rollback business logic
        executeRollback(outboxMessage);

        log.info("Room reservation rollback completed for booking: {}",
                outboxMessage.getBookingId());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Validate và lấy outbox message để tránh duplicate processing
     *
     * @param data RoomMessageResponse từ external service
     * @return BookingRoomOutboxMessage nếu hợp lệ, null nếu đã processed
     */
    private BookingRoomOutboxMessage validateAndGetOutboxMessage(RoomMessageResponse data) {
        Optional<BookingRoomOutboxMessage> outboxMessageOpt =
                roomOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);

        if (outboxMessageOpt.isEmpty()) {
            log.info("Outbox message with saga id: {} already processed!", data.getSagaId());
            return null;
        }

        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện business logic đặt phòng
     *
     * @param outboxMessage Outbox message chứa thông tin booking
     * @return BookingDepositEvent domain event
     */
    private BookingConfirmedEvent executeRoomReservation(BookingRoomOutboxMessage outboxMessage) {
        log.info("Executing room reservation for booking: {}", outboxMessage.getBookingId());

        // Tìm booking entity
        Booking booking = bookingSagaHelper.findBooking(outboxMessage.getBookingId());

        // Thực hiện business logic confirm deposit
        BookingConfirmedEvent domainEvent = bookingDomainService.confirmBooking(booking);

        // Lưu trạng thái mới
        bookingRepository.save(booking);

        return domainEvent;
    }


    /**
     * Trigger bước tiếp theo - send qr code notification
     *
     * @param domainEvent Domain event chứa thông tin booking
     * @param data        Room message từ message kafka
     */
    private void triggerSendQrCodeStep(BookingConfirmedEvent domainEvent, RoomMessageResponse data) {
        log.info("Triggering room reservation step for booking: {}",
                domainEvent.getBooking().getId().getValue());

        // Tạo notification outbox message để trigger notification send QR code step
        notificationOutboxHelper.saveNotificationOutboxMessage(
                notificationDataMapper.bookingRoomReservedEventToBookingNotifiEventPayload(domainEvent),
                domainEvent.getBooking().getStatus(),
                bookingSagaHelper.bookingStatusToSagaStatus(domainEvent.getBooking().getStatus()),
                OutboxStatus.STARTED,
                UUID.fromString(data.getSagaId()));
    }


    /**
     * Cập nhật saga status và lưu outbox message
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param domainEvent   Domain event chứa trạng thái mới
     */
    private void updateSagaStatusAndSaveOutbox(BookingRoomOutboxMessage outboxMessage,
                                               BookingConfirmedEvent domainEvent) {
        // Chuyển đổi booking status sang saga status
        SagaStatus sagaStatus = bookingSagaHelper.bookingStatusToSagaStatus(
                domainEvent.getBooking().getStatus());

        // Tạo outbox message mới với trạng thái đã cập nhật
        BookingRoomOutboxMessage updatedOutboxMessage =
                roomOutboxHelper.getUpdatedRoomOutBoxMessage(
                        outboxMessage,
                        domainEvent.getBooking().getStatus(),
                        sagaStatus);

        // Lưu outbox message
        roomOutboxHelper.save(updatedOutboxMessage);
    }

    /**
     * Tìm outbox message phù hợp để rollback
     *
     * @param data RoomMessageResponse chứa thông tin rollback
     * @return BookingRoomOutboxMessage nếu tìm thấy, null nếu đã rollback
     */
    private BookingRoomOutboxMessage findOutboxMessageForRollback(RoomMessageResponse data) {
        SagaStatus[] validStatuses = getValidSagaStatusesForRollback(data.getReservationStatus());

        Optional<BookingRoomOutboxMessage> outboxMessageOpt =
                roomOutboxHelper.getApprovalOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        validStatuses);

        if (outboxMessageOpt.isEmpty()) {
            log.info("Outbox message with saga id: {} already rolled back!", data.getSagaId());
            return null;
        }

        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện rollback business logic
     *
     * @param outboxMessage Outbox message cần rollback
     */
    private void executeRollback(BookingRoomOutboxMessage outboxMessage) {
        // Tìm booking entity
        Booking booking = bookingSagaHelper.findBooking(outboxMessage.getBookingId());

        // Thực hiện cancel booking
        bookingDomainService.cancelBooking(booking);

        // Lưu trạng thái đã cancel
        bookingRepository.save(booking);
    }

    /**
     * Xác định các saga status hợp lệ cho rollback dựa trên reservation status
     * <p>
     * LOGIC:
     * - SUCCESS: Chỉ rollback khi đang PROCESSING
     * - FAILED/CANCELLED: Rollback từ STARTED hoặc PROCESSING
     *
     * @param reservationStatus Trạng thái reservation từ external service
     * @return Array các SagaStatus hợp lệ cho rollback
     */
    private SagaStatus[] getValidSagaStatusesForRollback(ReservationStatus reservationStatus) {
        return switch (reservationStatus) {
            case SUCCESS -> new SagaStatus[]{SagaStatus.PROCESSING};
            case PENDING, FAILED, CANCELLED -> new SagaStatus[]{SagaStatus.STARTED, SagaStatus.PROCESSING};
        };
    }
}
