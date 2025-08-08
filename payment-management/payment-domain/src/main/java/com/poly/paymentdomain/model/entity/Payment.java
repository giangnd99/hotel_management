package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.valueobject.*;
import com.poly.paymentdomain.model.exception.AlreadyConfirmedPaymentException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Payment extends AggregateRoot<PaymentId> {
    private InvoiceId invoiceId;
    private BookingId bookingId;
    private PaymentStatus paymentStatus; // Trạng thái thanh toán
    private Money amount; // Tổng số tiền thanh toán
    private PaymentMethod method; // CASH, PAYOS
    private LocalDateTime paidAt; // Thời gian trả
    private LocalDateTime createdAt;
    private PaymentTransactionType paymentTransactionType; // Loại dịch vụ chi trả
    private PaymentReference referenceCode; // mã QR hoặc mã giao dịch

    private Payment(Builder builder) {
        this.setId(builder.paymentId);
        this.bookingId = builder.bookingId;
        this.invoiceId = builder.invoiceId != null ? builder.invoiceId : null;
        this.paymentStatus = builder.paymentStatus != null ? builder.paymentStatus : PaymentStatus.PENDING;
        this.amount = builder.amount;
        this.method = builder.method;
        this.paidAt = builder.paidAt;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.paymentTransactionType = builder.paymentTransactionType != null ? builder.paymentTransactionType : PaymentTransactionType.OTHER;
        this.referenceCode = builder.referenceCode != null ? builder.referenceCode : PaymentReference.empty();
    }

    public static final class Builder {
        private PaymentId paymentId;
        private InvoiceId invoiceId;
        private BookingId bookingId;
        private Money amount;
        private PaymentStatus paymentStatus;
        private PaymentMethod method;
        private LocalDateTime paidAt;
        private LocalDateTime createdAt;
        private PaymentTransactionType paymentTransactionType;
        private PaymentReference referenceCode;

        public Builder() {
        }

        public Builder id(PaymentId val) {
            paymentId = val;
            return this;
        }

        public Builder invoiceId(InvoiceId val) {
            invoiceId = val;
            return this;
        }

        public Builder bookingId(BookingId val) {
            bookingId = val;
            return this;
        }

        public Builder amount(Money val) {
            amount = val;
            return this;
        }

        public Builder method(PaymentMethod val) {
            method = val;
            return this;
        }

        public Builder paidAt(LocalDateTime val) {
            paidAt = val;
            return this;
        }

        public Builder createdAt(LocalDateTime val) {
            createdAt = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Builder paymentTransactionType(PaymentTransactionType val) {
            paymentTransactionType = val;
            return this;
        }

        public Builder referenceCode(PaymentReference val) {
            referenceCode = val;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public void markAsPaid(LocalDateTime paidAt) {
        if (this.paymentStatus == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot mark payment as COMPLETED from status: " + paymentStatus.getValue());
        }
        this.paymentStatus = PaymentStatus.COMPLETED;
        this.paidAt = paidAt;
    }

    public void markAsCancelled(LocalDateTime paidAt) {
        if (this.paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalStateException("Cannot mark payment as CANCELLED from status: " + paymentStatus);
        }
        this.paymentStatus = PaymentStatus.CANCELLED;
        this.paidAt = paidAt;
    }

    public void markAsFailed(LocalDateTime paidAt) {
        if (this.paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalStateException("Cannot mark payment as FAILED from status: " + paymentStatus);
        }
        this.paymentStatus = PaymentStatus.FAILED;
        this.paidAt = paidAt;
    }

    public void markAsExpired() {
        if (this.paymentStatus != PaymentStatus.PENDING) {
            return ;
        }
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(this.getCreatedAt(), now);
        if (duration.toMinutes() > 20) {
            this.paymentStatus = PaymentStatus.EXPIRED;
        }
    }

    public boolean isExpired() {
        if (this.createdAt == null) return false;
        return this.createdAt.plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public boolean isPaid() {
        return this.paymentStatus.equals(new PaymentStatus(PaymentStatus.Status.COMPLETED));
    }

}