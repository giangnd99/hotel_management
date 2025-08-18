package com.poly.payment.management.domain.dto.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class CreateInvoiceRequest {
    private UUID referenceId;
    private UUID customerId;
    private UUID staffId;
    private BigDecimal tax;
    private BigDecimal voucherAmount;
    private BigDecimal subTotal;
    private BigDecimal totalAmount;
    private String note;
}
