package com.poly.paymentapplicationservice.dto.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateInvoiceItemCommand {
    private UUID serviceId;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String note;
}
