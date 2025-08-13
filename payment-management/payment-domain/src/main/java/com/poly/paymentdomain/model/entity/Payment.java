package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.ReferenceId;
import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentdomain.model.entity.value_object.*;
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
}