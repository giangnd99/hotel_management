package com.poly.paymentapplicationservice.share;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class ItemData {
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
