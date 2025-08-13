package com.poly.paymentapplicationservice.dto.result;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class InvoiceResult {
    private UUID invoiceId;
    private UUID customerId;
    private UUID staffId;
    private BigDecimal subAmount;
    private BigDecimal totalAmount;
    private BigDecimal taxtRate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String note;
}
