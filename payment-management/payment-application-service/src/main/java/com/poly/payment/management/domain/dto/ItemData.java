package com.poly.payment.management.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ItemData {
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
