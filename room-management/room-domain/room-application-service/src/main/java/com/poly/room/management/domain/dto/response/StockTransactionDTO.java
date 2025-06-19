package com.poly.room.management.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockTransactionDTO {
    private String itemCode;
    private String type;
    private int quantity;
    private LocalDateTime timestamp;
    private String note;
}
