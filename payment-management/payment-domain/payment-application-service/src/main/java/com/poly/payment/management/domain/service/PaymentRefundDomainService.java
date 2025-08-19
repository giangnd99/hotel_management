package com.poly.payment.management.domain.service;

import com.poly.payment.management.domain.entity.Payment;
import com.poly.payment.management.domain.event.PaymentRefundEvent;
import com.poly.payment.management.domain.exception.PaymentDomainException;
import com.poly.domain.valueobject.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Payment Refund Domain Service - Xử lý business logic hoàn tiền
 * <p>
 * CHỨC NĂNG:
 * - Validate điều kiện hoàn tiền
 * - Xác định số tiền có thể hoàn
 * - Thực hiện hoàn tiền và tạo domain event
 * <p>
 * MỤC ĐÍCH:
 * - Đảm bảo business rules được tuân thủ khi hoàn tiền
 * - Xử lý logic hoàn tiền theo chính sách của khách sạn
 * - Cập nhật trạng thái payment một cách nhất quán
 */
@Slf4j
@Service
public class PaymentRefundDomainService {

    /**
     * Hoàn tiền với validation đầy đủ
     * <p>
     * BUSINESS RULES:
     * - Chỉ cho phép hoàn tiền payment có trạng thái hợp lệ
     * - Số tiền hoàn không được vượt quá số tiền đã thanh toán
     * - Payment phải đã được xác nhận thành công
     * <p>
     * VALIDATION:
     * - Kiểm tra trạng thái payment có thể hoàn tiền
     * - Kiểm tra số tiền hoàn có hợp lệ
     * - Xác định lý do hoàn tiền
     *
     * @param payment Payment cần hoàn tiền
     * @param refundAmount Số tiền cần hoàn
     * @param refundReason Lý do hoàn tiền
     * @return PaymentRefundEvent chứa thông tin hoàn tiền
     */
    public PaymentRefundEvent refundPayment(Payment payment, BigDecimal refundAmount, String refundReason) {
        log.info("Processing refund for payment: {} with amount: {} and reason: {}", 
                payment.getId().getValue(), refundAmount, refundReason);

        // Validate payment có thể hoàn tiền
        validatePaymentCanBeRefunded(payment, refundAmount);

        // Cập nhật trạng thái payment
        payment.setStatus(PaymentStatus.REFUNDED);

        // Tạo domain event
        PaymentRefundEvent refundEvent = new PaymentRefundEvent(payment, refundAmount, refundReason);

        log.info("Payment refunded successfully: {}. Amount: {}. Reason: {}", 
                payment.getId().getValue(), refundAmount, refundReason);

        return refundEvent;
    }

    /**
     * Validate payment có thể hoàn tiền hay không
     * <p>
     * CHECKS:
     * - Trạng thái payment phải hợp lệ để hoàn tiền
     * - Không thể hoàn tiền payment đã bị hoàn tiền
     * - Số tiền hoàn không được vượt quá số tiền đã thanh toán
     *
     * @param payment Payment cần validate
     * @param refundAmount Số tiền cần hoàn
     * @throws PaymentDomainException nếu không thể hoàn tiền
     */
    private void validatePaymentCanBeRefunded(Payment payment, BigDecimal refundAmount) {
        if (payment == null) {
            throw new PaymentDomainException("Payment cannot be null for refund");
        }

        if (refundAmount == null || refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentDomainException("Refund amount must be greater than 0");
        }

        PaymentStatus currentStatus = payment.getStatus();
        
        if (currentStatus == PaymentStatus.REFUNDED) {
            throw new PaymentDomainException("Payment is already refunded: " + payment.getId().getValue());
        }

        if (currentStatus == PaymentStatus.FAILED) {
            throw new PaymentDomainException("Cannot refund failed payment: " + payment.getId().getValue());
        }

        if (currentStatus == PaymentStatus.PENDING) {
            throw new PaymentDomainException("Cannot refund pending payment: " + payment.getId().getValue());
        }

        // Kiểm tra số tiền hoàn không vượt quá số tiền đã thanh toán
        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new PaymentDomainException("Refund amount cannot exceed payment amount: " + payment.getId().getValue());
        }

        log.debug("Payment validation passed for refund: {}", payment.getId().getValue());
    }

    /**
     * Kiểm tra xem có thể hoàn tiền payment hay không
     *
     * @param payment Payment cần kiểm tra
     * @param refundAmount Số tiền cần hoàn
     * @return true nếu có thể hoàn tiền
     */
    public boolean canRefundPayment(Payment payment, BigDecimal refundAmount) {
        try {
            validatePaymentCanBeRefunded(payment, refundAmount);
            return true;
        } catch (PaymentDomainException e) {
            log.debug("Payment cannot be refunded: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Tính toán số tiền hoàn tối đa có thể
     *
     * @param payment Payment cần tính toán
     * @return Số tiền hoàn tối đa
     */
    public BigDecimal getMaximumRefundAmount(Payment payment) {
        if (payment == null || payment.getStatus() != PaymentStatus.SUCCESS) {
            return BigDecimal.ZERO;
        }
        return payment.getAmount();
    }

    /**
     * Kiểm tra xem payment có đủ điều kiện hoàn tiền hay không
     *
     * @param payment Payment cần kiểm tra
     * @return true nếu đủ điều kiện
     */
    public boolean isEligibleForRefund(Payment payment) {
        if (payment == null) {
            return false;
        }

        PaymentStatus status = payment.getStatus();
        return status == PaymentStatus.SUCCESS || status == PaymentStatus.COMPLETED;
    }
}
