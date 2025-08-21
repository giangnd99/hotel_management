package com.poly.booking.management.domain.saga.room;

import com.poly.booking.management.domain.event.BookingConfirmedEvent;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

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
public class ReserveRoomStep implements SagaStep<RoomMessageResponse> {


    private final RoomSagaHelper roomSagaHelper;

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
    @Transactional
    public void process(RoomMessageResponse data) {
        log.info("Processing room reservation for saga id: {}", data.getSagaId());

        // Step 1: Validate & load outbox messages
        List<RoomOutboxMessage> outboxMessages = roomSagaHelper.validateAndGetOutboxMessage(data);
        if (outboxMessages.isEmpty()) {
            log.warn("No outbox message found or already processed for saga id: {}", data.getSagaId());
            return;
        }

        // Step 2: Lọc ra CONFIRMED messages
        List<RoomOutboxMessage> confirmedMessages = outboxMessages.stream()
                .filter(msg -> BookingStatus.DEPOSITED.equals(msg.getBookingStatus()))
                .toList();

        if (confirmedMessages.isEmpty()) {
            log.warn("No CONFIRMED outbox message found for booking: {}", data.getBookingId());
            return;
        }

        if (confirmedMessages.size() > 1) {
            log.error("Duplicate CONFIRMED room reservation detected for booking: {}", data.getBookingId());
        }

        // Lấy message đầu tiên
        RoomOutboxMessage outboxMessageFinal = confirmedMessages.get(0);

        // Step 3: Thực thi domain logic
        BookingConfirmedEvent domainEvent = roomSagaHelper.executeRoomReservation(outboxMessageFinal);

        // Step 4: Update saga state + outbox
        roomSagaHelper.updateSagaStatusAndSaveOutbox(outboxMessageFinal, domainEvent);

        // Step 5: Trigger bước tiếp theo (QR code check-in)
        roomSagaHelper.triggerSendQrCodeStep(domainEvent, data);

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
        RoomOutboxMessage outboxMessage = roomSagaHelper.findOutboxMessageForRollback(data);
        if (outboxMessage == null) {
            return; // Already rolled back
        }

        // Step 2: Execute rollback business logic
        roomSagaHelper.executeRollback(outboxMessage);

        log.info("Room reservation rollback completed for booking: {}",
                outboxMessage.getBookingId());
    }

}
