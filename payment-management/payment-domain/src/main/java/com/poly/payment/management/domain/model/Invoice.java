package com.poly.payment.management.domain.model;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.payment.management.domain.value_object.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Invoice extends AggregateRoot<InvoiceId> {
    private CustomerId customerId;
    private StaffId staffId;
    private Money subTotal;
    private Money totalAmount;
    private Money taxRate;
    @Setter
    private InvoiceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Description note;

    @Builder
    public Invoice(InvoiceId invoiceId, CustomerId customerId, StaffId staffId, Money subTotal, Money taxRate, Money totalAmount, InvoiceStatus invoiceStatus, LocalDateTime createdAt, LocalDateTime updatedAt, Description note) {
        this.setId(invoiceId);
        this.customerId = customerId;
        this.staffId = staffId;
        this.subTotal = subTotal;
        this.taxRate = taxRate;
        this.totalAmount = totalAmount;
        this.status = invoiceStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.note = note;
    }

    public void markAsPaid(LocalDateTime paidAt) {
        if (this.status == InvoiceStatus.PAID) {
            return;
        }
        if (this.status == InvoiceStatus.PENDING) {
            this.status = InvoiceStatus.PAID;
        }
        this.updatedAt = paidAt;
    }

    public void markAsFailed(LocalDateTime failedAt) {
        if (this.status == InvoiceStatus.PAID) {
            return;
        }
        this.status = InvoiceStatus.FAILED;
        this.updatedAt = failedAt;
    }
}
