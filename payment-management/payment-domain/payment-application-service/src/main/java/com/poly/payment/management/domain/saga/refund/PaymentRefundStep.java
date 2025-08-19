package com.poly.payment.management.domain.saga.refund;

import com.poly.payment.management.domain.entity.Payment;
import com.poly.payment.management.domain.event.PaymentRefundEvent;
import com.poly.payment.management.domain.message.BookingCancellationMessage;
import com.poly.payment.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Payment Refund Saga Step - Xử lý việc hoàn tiền
 * <p>
 * CHỨC NĂNG:
 * - Xử lý việc hoàn tiền trong quy trình Saga
 * - Quản lý trạng thái hoàn tiền và cập nhật outbox messages
 * - Thực hiện rollback khi có lỗi xảy ra
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo tính nhất quán dữ liệu trong quy trình hoàn tiền
 * - Xử lý bất đồng bộ thông qua Outbox Pattern
 * - Cung cấp khả năng rollback khi có lỗi
 * <p>
 * BUSINESS RULES:
 * - Nhận tin nhắn từ booking service để trigger hoàn tiền
 * - Kiểm tra điều kiện hoàn tiền và thực hiện hoàn tiền
 * - Cập nhật trạng thái payment sang REFUNDED
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRefundStep implements SagaStep<BookingCancellationMessage> {

    private final PaymentRefundSagaHelper paymentRefundSagaHelper;

    /**
     * PROCESS METHOD - Xử lý chính của Refund Saga Step
     * <p>
     * LOGIC FLOW:
     * 1. Validate outbox message để tránh duplicate processing
     * 2. Thực hiện business logic hoàn tiền
     * 3. Cập nhật saga status và lưu outbox messages
     * 4. Gửi thông báo hoàn tiền thành công
     * <p>
     * OUTBOX PATTERN:
     * - Sử dụng PaymentRefundSagaHelper để quản lý outbox messages
     * - Đảm bảo message được xử lý một cách reliable
     * - Tránh duplicate processing thông qua saga status check
     * <p>
     * SAGA PATTERN:
     * - Chuyển đổi payment status sang REFUNDED
     * - Cập nhật outbox message với trạng thái mới
     * - Gửi thông báo hoàn tiền thành công
     */
    @Override
    @Transactional
    public void process(BookingCancellationMessage data) {
        log.info("Processing payment refund for saga id: {}", data.getSagaId());

        // Step 1: Validate outbox message to prevent duplicate processing
        PaymentOutboxMessage outboxMessage = paymentRefundSagaHelper.validateAndGetOutboxMessage(data);
        if (outboxMessage == null) {
            return; // Already processed
        }

        // Step 2: Execute business logic - refund payment
        PaymentRefundEvent domainEvent = paymentRefundSagaHelper.executePaymentRefund(outboxMessage, data);

        // Step 3: Update saga status and save outbox message
        paymentRefundSagaHelper.updateSagaStatusAndSaveOutbox(outboxMessage, domainEvent);

        // Step 4: Send refund success notification
        paymentRefundSagaHelper.sendRefundSuccessNotification(domainEvent, data);

        log.info("Payment refund completed successfully for payment: {}",
                domainEvent.getPayment().getId().getValue());
    }

    /**
     * ROLLBACK METHOD - Xử lý rollback khi có lỗi
     * <p>
     * LOGIC FLOW:
     * 1. Tìm outbox message với trạng thái phù hợp
     * 2. Thực hiện revert refund
     * 3. Cập nhật trạng thái đã rollback
     * <p>
     * SAGA COMPENSATION:
     * - Thực hiện compensation action khi có lỗi
     * - Đảm bảo tính nhất quán dữ liệu
     * - Revert payment status về trạng thái trước đó
     */
    @Override
    @Transactional
    public void rollback(BookingCancellationMessage data) {
        log.info("Rolling back payment refund for saga id: {}", data.getSagaId());

        // Step 1: Validate outbox message for rollback
        PaymentOutboxMessage outboxMessage = paymentRefundSagaHelper.validateAndGetOutboxMessageForRollback(data);
        if (outboxMessage == null) {
            return; // No outbox message found for rollback
        }

        // Step 2: Execute rollback business logic
        paymentRefundSagaHelper.executeRollbackBusinessLogic(data);

        // Step 3: Update outbox message for rollback
        paymentRefundSagaHelper.updateOutboxMessageForRollback(outboxMessage, data);

        log.info("Payment refund rollback completed for saga: {}", data.getSagaId());
    }
}
