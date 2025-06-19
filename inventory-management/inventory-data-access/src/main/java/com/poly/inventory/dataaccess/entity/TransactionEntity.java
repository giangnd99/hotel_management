package com.poly.inventory.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_transactions")
@Data
public class TransactionEntity {
    @Id
    @Column(name = "transaction_id")
    private Integer id;

    private Integer itemId;
    private Integer staffId;
    private String transactionType; // IN, OUT, CHECK
    private Integer quantity;
    private LocalDateTime transactionDate;
}
