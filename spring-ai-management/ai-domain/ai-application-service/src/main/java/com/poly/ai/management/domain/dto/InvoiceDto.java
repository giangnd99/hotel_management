package com.poly.ai.management.domain.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {

    private UUID id;

    private UUID customerId;

    private UUID staffId;

    private BigDecimal taxRate;

    private BigDecimal subTotal;

    private BigDecimal totalAmount;

    private String status;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private String note;
}
