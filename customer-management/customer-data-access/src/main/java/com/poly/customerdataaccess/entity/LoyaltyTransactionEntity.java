package com.poly.customerdataaccess.entity;

import com.poly.customerdomain.model.entity.valueobject.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loyalty_transaction")
@Getter
@Setter
public class LoyaltyTransactionEntity {

    @Id
    @Column(name = "transaction_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "loyalty_id", nullable = false)
    private UUID loyaltyPointId;

    @Column(name = "points_changed", nullable = false)
    private BigDecimal pointChange;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(name = "description")
    private String description;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
}
