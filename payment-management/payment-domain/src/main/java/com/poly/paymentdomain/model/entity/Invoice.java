package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Invoice extends AggregateRoot<InvoiceId> {
    private CustomerId customerId;
    private StaffId createdBy;
    private Money subTotal;
    private Money taxRate;
    private Money totalAmount;
    private InvoiceStatus invoiceStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Description note;

    @Builder
    public Invoice(InvoiceId invoiceId, CustomerId customerId, StaffId createdBy, Money subTotal, Money taxRate, Money totalAmount, InvoiceStatus invoiceStatus, LocalDateTime createdAt, LocalDateTime updatedAt, Description note) {
        this.setId(invoiceId);
        this.customerId = customerId;
        this.createdBy = createdBy;
        this.subTotal = subTotal;
        this.taxRate = taxRate;
        this.totalAmount = totalAmount;
        this.invoiceStatus = invoiceStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.note = note;
    }

    // —— Query helpers
    public boolean isDraft()   { return invoiceStatus == InvoiceStatus.DRAFT; }
    public boolean isPending() { return invoiceStatus == InvoiceStatus.PENDING; }
    public boolean isPaid()    { return invoiceStatus == InvoiceStatus.PAID; }
}
