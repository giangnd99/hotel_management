package com.poly.booking.management.domain.saga.payment;

import com.poly.booking.management.domain.dto.message.PaymentMessageResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.event.BookingDepositedEvent;
import com.poly.booking.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * BookingPaymentSaga - Saga Step Implementation for Payment Processing
 * <p>
 * CHỨC NĂNG CHÍNH:
 * - Xử lý thanh toán trong quy trình Saga của hệ thống booking
 * - Quản lý trạng thái thanh toán và cập nhật outbox messages
 * - Thực hiện rollback khi thanh toán thất bại
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo tính nhất quán dữ liệu trong quy trình thanh toán
 * - Xử lý bất đồng bộ thông qua Outbox Pattern
 * - Cung cấp khả năng rollback khi có lỗi thanh toán
 * <p>
 * ÁP DỤNG PATTERNS:
 * - Saga Pattern: Quản lý distributed transaction cho payment flow
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Domain Events: Tách biệt business logic payment
 * <p>
 * FLOW XỬ LÝ:
 * 1. Nhận payment response từ external payment service
 * 2. Validate outbox message để tránh duplicate processing
 * 3. Thực hiện business logic thanh toán
 * 4. Cập nhật saga status và lưu outbox messages
 * 5. Trigger next step (room reservation) nếu thanh toán thành công
 * <p>
 * ROLLBACK FLOW:
 * 1. Nhận payment failure/cancellation từ external service
 * 2. Tìm outbox message với trạng thái phù hợp
 * 3. Thực hiện cancel booking
 * 4. Cập nhật trạng thái đã rollback
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DepositStep implements SagaStep<PaymentMessageResponse> {

    // ==================== DEPENDENCIES ====================

    // Business logic services
    private final DepositSagaHelper depositSagaHelper;

    // ==================== SAGA STEP IMPLEMENTATION ====================

    /**
     * PROCESS METHOD - Xử lý chính của Payment Saga Step
     * <p>
     * LOGIC FLOW:
     * 1. Validate outbox message để tránh duplicate processing
     * 2. Thực hiện business logic thanh toán
     * 3. Cập nhật saga status và lưu outbox messages
     * 4. Trigger next step (room reservation) nếu thành công
     * <p>
     * OUTBOX PATTERN:
     * - Sử dụng PaymentOutboxHelper để quản lý payment outbox messages
     * - Đảm bảo payment message được xử lý một cách reliable
     * - Tránh duplicate processing thông qua saga status check
     * <p>
     * SAGA PATTERN:
     * - Chuyển đổi booking status sang saga status
     * - Cập nhật payment outbox message với trạng thái mới
     * - Trigger room reservation step nếu payment thành công
     */
    @Override
    @Transactional
    public void process(PaymentMessageResponse paymentResponse) {
        log.info("Processing deposit payment for saga id: {}", paymentResponse.getSagaId());

        // Step 1: Validate outbox message to prevent duplicate processing
        PaymentOutboxMessage outboxMessage = depositSagaHelper.validateAndGetPaymentOutboxMessage(paymentResponse);
        if (outboxMessage == null) {
            return; // Already processed
        }

        // Step 2: Execute business logic - complete payment
        BookingDepositedEvent domainEvent = depositSagaHelper.executePaymentCompletion(paymentResponse);

        // Step 3: Update saga status and save payment outbox message
        depositSagaHelper.updatePaymentOutboxMessage(outboxMessage, domainEvent);

        // Step 4: Trigger next step - room reservation
        depositSagaHelper.triggerRoomReservationStep(domainEvent, paymentResponse.getSagaId());

        log.info("Payment processing completed successfully for booking: {}",
                domainEvent.getBooking().getId().getValue());
    }

    /**
     * ROLLBACK METHOD - Xử lý rollback khi thanh toán thất bại
     * <p>
     * LOGIC FLOW:
     * 1. Tìm payment outbox message với trạng thái phù hợp
     * 2. Thực hiện cancel booking
     * 3. Cập nhật trạng thái đã rollback
     * 4. Rollback room reservation nếu cần
     * <p>
     * SAGA COMPENSATION:
     * - Thực hiện compensation action khi payment thất bại
     * - Đảm bảo tính nhất quán dữ liệu
     * - Rollback các bước đã thực hiện trước đó
     */
    @Override
    @Transactional
    public void rollback(PaymentMessageResponse paymentResponse) {
        log.info("Rolling back payment for saga id: {}", paymentResponse.getSagaId());

        // Step 1: Find payment outbox message for rollback
        PaymentOutboxMessage outboxMessage = depositSagaHelper.findPaymentOutboxMessageForRollback(paymentResponse);
        if (outboxMessage == null) {
            return; // Already rolled back
        }

        // Step 2: Execute rollback business logic
        Booking booking = depositSagaHelper.executePaymentRollback(paymentResponse);

        // Step 3: Update payment outbox message with rollback status
        depositSagaHelper.updatePaymentOutboxMessageForRollback(outboxMessage, booking);

        // Step 4: Rollback room reservation if payment was cancelled
        if (paymentResponse.getPaymentStatus() == PaymentStatus.CANCELLED) {
            depositSagaHelper.rollbackRoomReservation(paymentResponse.getSagaId(), booking);
        }

        log.info("Payment rollback completed successfully for booking: {}",
                booking.getId().getValue());
    }


}

