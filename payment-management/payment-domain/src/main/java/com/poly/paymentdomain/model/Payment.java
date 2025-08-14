package com.poly.paymentdomain.model;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.ReferenceId;
import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentdomain.model.value_object.Description;
import com.poly.paymentdomain.model.value_object.Money;
import com.poly.paymentdomain.model.value_object.OrderCode;
import com.poly.paymentdomain.model.value_object.PaymentId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Payment extends AggregateRoot<PaymentId> {
    private ReferenceId referenceId;
    @Setter
    private PaymentStatus status;
    private Money amount;
    private PaymentMethod method;
    @Setter
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderCode orderCode;
    @Setter
    private String paymentLink;
    private Description description;

    @Builder
    public Payment(PaymentId paymentId, ReferenceId referenceId, PaymentStatus status, Money amount, PaymentMethod method, LocalDateTime paidAt, LocalDateTime createdAt, LocalDateTime updatedAt, OrderCode orderCode, String paymentLink, Description description) {
        this.setId(paymentId);
        this.referenceId = referenceId;
        this.status = status;
        this.amount = amount;
        this.method = method;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderCode = orderCode;
        this.paymentLink = paymentLink;
        this.description = description;
    }

    public void markAsPaid(LocalDateTime paidAt) {
        if (this.status == PaymentStatus.PAID) {
            return;
        }
        this.status = PaymentStatus.PAID;
        this.paidAt = paidAt;
        this.updatedAt = paidAt;
    }

    public void markAsFailed(LocalDateTime paidAt) {
        if (this.status == PaymentStatus.PAID) {
            return;
        }
        this.status = PaymentStatus.FAILED;
        this.paidAt = paidAt;
        this.updatedAt = paidAt;
    }

    public void markAsExpired() {
        if (this.status == PaymentStatus.EXPIRED) {
            return;
        }
        this.status = PaymentStatus.EXPIRED;
        this.updatedAt = LocalDateTime.now();
    }
}