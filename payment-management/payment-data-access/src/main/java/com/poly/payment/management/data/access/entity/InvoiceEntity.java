package com.poly.payment.management.data.access.entity;

import com.poly.payment.management.domain.value_object.InvoiceStatus;
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

    @Column(name = "staff_id", columnDefinition = "BINARY(16)")
    private UUID staffId;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    @Column(name = "sub_total")
    private BigDecimal subTotal;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status;

    @Column(name = "create_at")
    private LocalDateTime createdDate;

    @Column(name = "update_at")
    private LocalDateTime updatedDate;

    @Column(name = "note")
    private String note;
}
