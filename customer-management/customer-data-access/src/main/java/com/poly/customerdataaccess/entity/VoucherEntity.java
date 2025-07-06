package com.poly.customerdataaccess.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "voucher")
public class VoucherEntity {

    @Id
    @Column(name = "voucher_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "promotion_id")
    private Integer promotionId;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "discount_percentage", nullable = false)
    private Double discountPercentage;

    @Column(name = "discount_amount", nullable = false)
    private Double discountAmount;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VoucherStatus status;
}
