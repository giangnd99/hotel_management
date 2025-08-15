package com.poly.paymentcontainer.dto;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class CreateDirectPaymentRequest {
    private @NonNull List<ItemRequest> items;
    private UUID referenceId;
    private UUID staffId;
    private UUID customerId;
    private BigDecimal subTotal;
    private BigDecimal voucherAmount;
    private BigDecimal taxRate;
    private String notes;
}
