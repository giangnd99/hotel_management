package com.poly.paymentdataaccess.entity;

import com.poly.paymentdataaccess.share.InvoiceStatusEntity;
import com.poly.paymentdomain.model.entity.valueobject.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoice")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceEntity {

    @Id
    @Column(name = "invoice_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "booking_id", columnDefinition = "BINARY(16)")
    private UUID bookingId;

    @Column(name = "customer_id", columnDefinition = "BINARY(16)")
    private UUID customerId;

    @Column(name = "created_by", columnDefinition = "BINARY(16)")
    private UUID createdByStaffId;

    @Column(name = "updated_by", columnDefinition = "BINARY(16)")
    private UUID updateByStaffId;

    @Column(name = "voucher_id", columnDefinition = "BINARY(16)")
    private UUID voucherId;

    @Column(name = "sub_total")
    private BigDecimal subTotal;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_status", nullable = false)
    private InvoiceStatusEntity invoiceStatusEntity;

    @Column(name = "create_at")
    private LocalDateTime createdDate;

    @Column(name = "update_at")
    private LocalDateTime updatedDate;

    @Column(name = "change_amount")
    private BigDecimal changeAmount;

    @Column(name = "note")
    private String note;
}
