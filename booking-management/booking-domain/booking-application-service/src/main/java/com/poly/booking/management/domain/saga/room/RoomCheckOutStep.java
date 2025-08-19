package com.poly.booking.management.domain.saga.room;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.CheckOutEvent;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.saga.SagaStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Room Check Out Saga Step - Xử lý việc checkout phòng
 * <p>
 * CHỨC NĂNG:
 * - Xử lý việc checkout phòng trong quy trình Saga của hệ thống booking
 * - Quản lý trạng thái checkout và cập nhật outbox messages
 * - Thực hiện rollback khi có lỗi xảy ra
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo tính nhất quán dữ liệu trong quy trình checkout
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
public class RoomCheckOutStep implements SagaStep<RoomMessageResponse> {

    private final RoomCheckOutSagaHelper roomCheckOutSagaHelper;

    /**
     * PROCESS METHOD - Xử lý chính của Saga Step
     * <p>
     * LOGIC:
     * 1. Kiểm tra outbox message để tránh duplicate processing
     * 2. Thực hiện business logic checkout
     * 3. Cập nhật trạng thái Saga và lưu outbox message
     * <p>
     * OUTBOX PATTERN:
     * - Sử dụng RoomCheckOutSagaHelper để quản lý outbox messages
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
        log.info("Processing room check out for saga id: {}", data.getSagaId());

        // Step 1: Validate outbox message to prevent duplicate processing
        RoomOutboxMessage outboxMessage = roomCheckOutSagaHelper.validateAndGetOutboxMessage(data);
        if (outboxMessage == null) {
            return; // Already processed
        }

        // Step 2: Execute business logic - check out room
        CheckOutEvent domainEvent = roomCheckOutSagaHelper.executeRoomCheckOut(outboxMessage);

        // Step 3: Update saga status and save outbox message
        roomCheckOutSagaHelper.updateSagaStatusAndSaveOutbox(outboxMessage, domainEvent);

        log.info("Room check out completed successfully for booking: {}",
                domainEvent.getBooking().getId().getValue());
    }

    /**
     * ROLLBACK METHOD - Xử lý rollback khi có lỗi
     * <p>
     * LOGIC:
     * 1. Tìm outbox message với trạng thái phù hợp
     * 2. Thực hiện revert checkout
     * 3. Lưu trạng thái đã rollback
     * <p>
     * SAGA COMPENSATION:
     * - Thực hiện compensation action khi có lỗi
     * - Đảm bảo tính nhất quán dữ liệu
     */
    @Override
    @Transactional
    public void rollback(RoomMessageResponse data) {
        log.info("Rolling back room check out for saga id: {}", data.getSagaId());

        // Step 1: Validate outbox message for rollback
        RoomOutboxMessage outboxMessage = roomCheckOutSagaHelper.validateAndGetOutboxMessageForRollback(data);
        if (outboxMessage == null) {
            return; // No outbox message found for rollback
        }

        // Step 2: Execute rollback business logic
        roomCheckOutSagaHelper.executeRollbackBusinessLogic(data);

        // Step 3: Update outbox message for rollback
        roomCheckOutSagaHelper.updateOutboxMessageForRollback(outboxMessage, data);

        log.info("Room check out rollback completed for booking: {}", data.getBookingId());
    }
}
