package com.poly.booking.management.domain.saga.cancellation;

import com.poly.booking.management.domain.event.BookingCancelledEvent;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Booking Cancellation Saga Step - Xử lý việc hủy booking
 * <p>
 * CHỨC NĂNG:
 * - Xử lý việc hủy booking trong quy trình Saga
 * - Quản lý trạng thái hủy và cập nhật outbox messages
 * - Thực hiện rollback khi có lỗi xảy ra
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo tính nhất quán dữ liệu trong quy trình hủy booking
 * - Xử lý bất đồng bộ thông qua Outbox Pattern
 * - Cung cấp khả năng rollback khi có lỗi
 * <p>
 * BUSINESS RULES:
 * - Nhận tin nhắn từ room message response để trigger hủy
 * - Kiểm tra thời gian: nếu cách check-in 1 ngày → không hoàn tiền
 * - Gửi tin nhắn hoàn tiền đến payment service nếu đủ điều kiện
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCancellationStep implements SagaStep<RoomMessageResponse> {

    private final BookingCancellationSagaHelper bookingCancellationSagaHelper;

    /**
     * PROCESS METHOD - Xử lý chính của Cancellation Saga Step
     * <p>
     * LOGIC FLOW:
     * 1. Validate outbox message để tránh duplicate processing
     * 2. Thực hiện business logic hủy booking
     * 3. Cập nhật saga status và lưu outbox messages
     * 4. Trigger next step (refund processing) nếu cần hoàn tiền
     * <p>
     * OUTBOX PATTERN:
     * - Sử dụng BookingCancellationSagaHelper để quản lý outbox messages
     * - Đảm bảo message được xử lý một cách reliable
     * - Tránh duplicate processing thông qua saga status check
     * <p>
     * SAGA PATTERN:
     * - Chuyển đổi booking status sang CANCELLED
     * - Cập nhật outbox message với trạng thái mới
     * - Trigger refund step nếu cần hoàn tiền
     */
    @Override
    @Transactional
    public void process(RoomMessageResponse data) {
        log.info("Processing booking cancellation for saga id: {}", data.getSagaId());

        // Step 1: Validate outbox message to prevent duplicate processing
        RoomOutboxMessage outboxMessage = bookingCancellationSagaHelper.validateAndGetOutboxMessage(data);
        if (outboxMessage == null) {
            return; // Already processed
        }

        // Step 2: Execute business logic - cancel booking
        BookingCancelledEvent domainEvent = bookingCancellationSagaHelper.executeBookingCancellation(outboxMessage, data);

        // Step 3: Update saga status and save outbox message
        bookingCancellationSagaHelper.updateSagaStatusAndSaveOutbox(outboxMessage, domainEvent);

        // Step 4: Trigger next step - refund processing nếu cần hoàn tiền
        if (domainEvent.isRefundable()) {
            bookingCancellationSagaHelper.triggerRefundStep(domainEvent, data);
        } else {
            log.info("Booking cancellation completed without refund for booking: {}", 
                    domainEvent.getBooking().getId().getValue());
        }

        log.info("Booking cancellation completed successfully for booking: {}",
                domainEvent.getBooking().getId().getValue());
    }

    /**
     * ROLLBACK METHOD - Xử lý rollback khi có lỗi
     * <p>
     * LOGIC FLOW:
     * 1. Tìm outbox message với trạng thái phù hợp
     * 2. Thực hiện revert cancellation
     * 3. Cập nhật trạng thái đã rollback
     * <p>
     * SAGA COMPENSATION:
     * - Thực hiện compensation action khi có lỗi
     * - Đảm bảo tính nhất quán dữ liệu
     * - Revert booking status về trạng thái trước đó
     */
    @Override
    @Transactional
    public void rollback(RoomMessageResponse data) {
        log.info("Rolling back booking cancellation for saga id: {}", data.getSagaId());

        // Step 1: Validate outbox message for rollback
        RoomOutboxMessage outboxMessage = bookingCancellationSagaHelper.validateAndGetOutboxMessageForRollback(data);
        if (outboxMessage == null) {
            return; // No outbox message found for rollback
        }

        // Step 2: Execute rollback business logic
        bookingCancellationSagaHelper.executeRollbackBusinessLogic(data);

        // Step 3: Update outbox message for rollback
        bookingCancellationSagaHelper.updateOutboxMessageForRollback(outboxMessage, data);

        log.info("Booking cancellation rollback completed for booking: {}", data.getBookingId());
    }
}
