package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Payment extends AggregateRoot<PaymentId> {
    private BookingId bookingId;
    private PaymentStatus paymentStatus;
    private Money amount;
    private PaymentMethod method;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private PaymentReference referenceCode;
    private PaymentTransactionType paymentTransactionType;


    @Builder
    public Payment(PaymentId paymentId, BookingId bookingId, PaymentStatus paymentStatus, Money amount, PaymentMethod method, LocalDateTime paidAt, LocalDateTime createdAt, PaymentReference referenceCode,  PaymentTransactionType paymentTransactionType) {
        this.setId(paymentId);
        this.bookingId = bookingId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.method = method;
        this.paidAt = paidAt;
        this.createdAt = createdAt;
        this.referenceCode = referenceCode;
        this.paymentTransactionType = paymentTransactionType;
    }
}