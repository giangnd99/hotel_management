package com.poly.customerdataaccess.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loyalty")
@Getter
@Setter
public class LoyaltyEntity {

    @Id
    @Column(name = "loyalty_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "points", nullable = false)
    private BigDecimal points;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
}
