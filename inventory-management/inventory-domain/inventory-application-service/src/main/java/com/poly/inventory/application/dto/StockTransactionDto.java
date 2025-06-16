package com.poly.inventory.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockTransactionDto {
    private Integer transactionId;
    private Integer itemId;
    private Integer staffId;
    private String transactionType;
    private Integer quantity;
    private LocalDateTime transactionDate;
}
