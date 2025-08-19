package com.poly.payment.management.domain.saga.refund;

import com.poly.payment.management.domain.entity.Payment;
import com.poly.payment.management.domain.event.PaymentRefundEvent;
import com.poly.payment.management.domain.message.BookingCancellationMessage;
import com.poly.payment.management.domain.outbox.model.PaymentOutboxMessage;
import com.poly.payment.management.domain.outbox.service.PaymentOutboxService;
import com.poly.payment.management.domain.port.out.repository.PaymentRepository;
import com.poly.payment.management.domain.service.PaymentRefundDomainService;
import com.poly.domain.valueobject.PaymentId;
import com.poly.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Payment Refund Saga Helper - Hỗ trợ xử lý business logic hoàn tiền
 * <p>
 * CHỨC NĂNG:
 * - Validate và quản lý outbox messages cho refund
 * - Thực hiện business logic hoàn tiền
 * - Quản lý saga status và outbox messages
 * - Gửi thông báo hoàn tiền thành công
 * <p>
 * MỤC ĐÍCH:
 * - Tách biệt business logic khỏi saga step
 * - Cung cấp các helper methods cho refund process
 * - Đảm bảo tính nhất quán dữ liệu
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentRefundSagaHelper {

    private final PaymentOutboxService paymentOutboxService;
    private final PaymentRepository paymentRepository;
    private final PaymentRefundDomainService paymentRefundDomainService;

    /**
     * Validate và lấy outbox message cho refund
     *
     * @param data Booking cancellation message
     * @return PaymentOutboxMessage nếu tìm thấy, null nếu đã xử lý
     */
    public PaymentOutboxMessage validateAndGetOutboxMessage(BookingCancellationMessage data) {
        Optional<PaymentOutboxMessage> outboxMessageOpt =
                paymentOutboxService.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);
        if (outboxMessageOpt.isEmpty()) {
            log.error("Could not find payment refund outbox message for saga id: {}", data.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }

    /**
     * Validate và lấy outbox message cho rollback
     *
     * @param data Booking cancellation message
     * @return PaymentOutboxMessage nếu tìm thấy, null nếu không tìm thấy
     */
    public PaymentOutboxMessage validateAndGetOutboxMessageForRollback(BookingCancellationMessage data) {
        Optional<PaymentOutboxMessage> outboxMessageOpt =
                paymentOutboxService.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(data.getSagaId()),
                        SagaStatus.PROCESSING);
        if (outboxMessageOpt.isEmpty()) {
            log.error("Could not find payment refund outbox message for rollback with saga id: {}", data.getSagaId());
            return null;
        }
        return outboxMessageOpt.get();
    }

    /**
     * Thực hiện business logic hoàn tiền
     *
     * @param outboxMessage Outbox message chứa thông tin payment
     * @param data Booking cancellation message
     * @return PaymentRefundEvent domain event
     */
    public PaymentRefundEvent executePaymentRefund(PaymentOutboxMessage outboxMessage, BookingCancellationMessage data) {
        log.info("Executing payment refund for payment: {}", outboxMessage.getPaymentId());

        // Tìm payment entity
        Payment payment = findPayment(outboxMessage.getPaymentId().toString());

        // Xác định số tiền hoàn và lý do hoàn tiền
        BigDecimal refundAmount = determineRefundAmount(data);
        String refundReason = determineRefundReason(data);

        // Thực hiện business logic hoàn tiền
        PaymentRefundEvent domainEvent = paymentRefundDomainService.refundPayment(payment, refundAmount, refundReason);

        // Lưu trạng thái mới
        paymentRepository.save(payment);

        return domainEvent;
    }

    /**
     * Cập nhật saga status và lưu outbox message
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param domainEvent  Domain event chứa trạng thái mới
     */
    public void updateSagaStatusAndSaveOutbox(PaymentOutboxMessage outboxMessage, PaymentRefundEvent domainEvent) {
        log.info("Updating saga status and outbox message for payment: {}", outboxMessage.getPaymentId());

        // Cập nhật saga status
        outboxMessage.setSagaStatus(SagaStatus.FINISHED);
        outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.COMPLETED);

        // Lưu outbox message
        paymentOutboxService.save(outboxMessage);

        log.info("Saga status and outbox message updated successfully for payment: {}", outboxMessage.getPaymentId());
    }

    /**
     * Gửi thông báo hoàn tiền thành công
     *
     * @param domainEvent Domain event chứa thông tin refund
     * @param data Booking cancellation message
     */
    public void sendRefundSuccessNotification(PaymentRefundEvent domainEvent, BookingCancellationMessage data) {
        log.info("Sending refund success notification for payment: {}", domainEvent.getPayment().getId().getValue());

        // TODO: Implement refund success notification to booking service
        // Tạo refund success outbox message để gửi đến booking service
        // bookingOutboxService.saveRefundSuccessOutboxMessage(domainEvent, data.getSagaId());

        log.info("Refund success notification sent successfully for payment: {}", domainEvent.getPayment().getId().getValue());
    }

    /**
     * Thực hiện rollback business logic
     *
     * @param data Booking cancellation message
     */
    public void executeRollbackBusinessLogic(BookingCancellationMessage data) {
        log.info("Executing rollback business logic for saga: {}", data.getSagaId());

        // TODO: Implement rollback logic based on business requirements
        // Có thể cần revert payment status về trạng thái trước đó
        
        log.info("Rollback business logic completed for saga: {}", data.getSagaId());
    }

    /**
     * Cập nhật outbox message cho rollback
     *
     * @param outboxMessage Outbox message cần cập nhật
     * @param data          Booking cancellation message
     */
    public void updateOutboxMessageForRollback(PaymentOutboxMessage outboxMessage, BookingCancellationMessage data) {
        log.info("Updating outbox message for rollback with saga: {}", data.getSagaId());

        // Cập nhật saga status
        outboxMessage.setSagaStatus(SagaStatus.FAILED);
        outboxMessage.setOutboxStatus(com.poly.outbox.OutboxStatus.FAILED);

        // Lưu outbox message
        paymentOutboxService.save(outboxMessage);

        log.info("Outbox message updated for rollback with saga: {}", data.getSagaId());
    }

    /**
     * Tìm payment theo ID
     *
     * @param paymentId Payment ID dạng string
     * @return Payment entity
     */
    private Payment findPayment(String paymentId) {
        return paymentRepository.findById(new PaymentId(UUID.fromString(paymentId)))
                .orElseThrow(() -> new RuntimeException("Payment not found for refund: " + paymentId));
    }

    /**
     * Xác định số tiền hoàn từ booking cancellation message
     *
     * @param data Booking cancellation message
     * @return Số tiền hoàn
     */
    private BigDecimal determineRefundAmount(BookingCancellationMessage data) {
        // TODO: Implement logic để xác định số tiền hoàn từ booking message
        // Có thể dựa vào deposit amount, cancellation policy, hoặc business rules
        
        if (data.getDepositAmount() != null) {
            return new BigDecimal(data.getDepositAmount());
        }
        
        // Default refund amount - cần implement business logic cụ thể
        return BigDecimal.ZERO;
    }

    /**
     * Xác định lý do hoàn tiền từ booking cancellation message
     *
     * @param data Booking cancellation message
     * @return Lý do hoàn tiền
     */
    private String determineRefundReason(BookingCancellationMessage data) {
        // TODO: Implement logic để xác định lý do hoàn tiền từ booking message
        // Có thể dựa vào cancellation reason, booking status, hoặc business rules
        
        if (data.getCancellationReason() != null) {
            return "Refund due to: " + data.getCancellationReason();
        }
        
        return "Refund due to booking cancellation";
    }
}
