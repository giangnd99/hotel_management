package com.poly.payment.management.domain.event;

import com.poly.payment.management.domain.entity.Payment;
import com.poly.domain.event.DomainEvent;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Payment Refund Event - Sự kiện hoàn tiền
 * <p>
 * CHỨC NĂNG:
 * - Đánh dấu payment đã được hoàn tiền
 * - Chứa thông tin về số tiền hoàn và thời gian hoàn tiền
 * - Trigger các business logic liên quan đến hoàn tiền
 * <p>
 * MỤC ĐÍCH:
 * - Thông báo cho các service khác về việc hoàn tiền
 * - Xử lý compensation logic (cập nhật trạng thái payment)
 * - Cập nhật trạng thái hệ thống
 */
public class PaymentRefundEvent implements DomainEvent<Payment> {

    private final Payment payment;
    private final BigDecimal refundAmount;
    private final String refundReason;
    private final ZonedDateTime refundedAt;

    public PaymentRefundEvent(Payment payment, BigDecimal refundAmount, String refundReason) {
        this.payment = payment;
        this.refundAmount = refundAmount;
        this.refundReason = refundReason;
        this.refundedAt = ZonedDateTime.now();
    }

    public Payment getPayment() {
        return payment;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public ZonedDateTime getRefundedAt() {
        return refundedAt;
    }
}
