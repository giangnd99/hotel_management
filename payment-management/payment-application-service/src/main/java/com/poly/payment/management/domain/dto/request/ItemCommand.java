package com.poly.payment.management.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ItemCommand {
    private UUID id;
    private String name;
    private BigDecimal amount;
    private Integer quantity;
}
