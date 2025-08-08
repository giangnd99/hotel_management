package com.poly.paymentapplicationservice.share;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class ItemData {
    private UUID serviceId;
    private @NonNull String name;
    private @NonNull Integer quantity;
    private @NonNull BigDecimal price;
    private String serviceType;
}
