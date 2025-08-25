package com.poly.restaurant.application.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDTO(
        String id,
        String customerId,
        String tableId,
        List<OrderItem> items,
        String status,
        LocalDateTime createdAt,
        String customerNote,
        String orderNumber
) {
    public record OrderItem(
            String menuItemId,
            Integer quantity,
            BigDecimal price
    ) {
    }
}
