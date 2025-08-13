package com.poly.restaurant.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        String id,
        String customerId,
        String tableId,
        List<OrderItem> items,
        String status,
        LocalDateTime createdAt,
        String customerNote
) {
    public record OrderItem(
            String menuItemId,
            Integer quantity,
            BigDecimal price
    ) {
    }
}
