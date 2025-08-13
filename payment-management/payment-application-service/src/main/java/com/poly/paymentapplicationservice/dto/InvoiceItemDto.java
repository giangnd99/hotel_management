package com.poly.paymentapplicationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class InvoiceItemDto {
    private UUID serviceId;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private LocalDateTime usedAt;
    private String note;
}
