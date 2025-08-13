package com.poly.paymentdataaccess.entity;

import com.poly.paymentdataaccess.share.InvoiceStatusEntity;
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
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "customer_id", columnDefinition = "BINARY(16)")
    private UUID customerId;

    @Column(name = "created_by", columnDefinition = "BINARY(16)")
    private UUID createdByStaffId;

    @Column(name = "sub_total")
    private BigDecimal subTotal;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_status", nullable = false)
    private InvoiceStatusEntity invoiceStatusEntity;

    @Column(name = "create_at")
    private LocalDateTime createdDate;

    @Column(name = "update_at")
    private LocalDateTime updatedDate;

    @Column(name = "note")
    private String note;
}
