package com.poly.payment.management.domain.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResult {
    private UUID invoiceId;
    private UUID customerId;
    private UUID staffId;
    private BigDecimal subAmount;
    private BigDecimal totalAmount;
    private BigDecimal taxRate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String note;
}
