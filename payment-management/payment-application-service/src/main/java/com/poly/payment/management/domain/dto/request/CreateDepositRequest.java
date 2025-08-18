package com.poly.payment.management.domain.dto.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class CreateDepositRequest {
    private UUID referenceId;
    private BigDecimal amount;
    private List<ItemRequest> items;
    private String description;
}
