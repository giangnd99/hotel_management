package com.poly.restaurant.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(
        Integer orderId,
        Integer tableId,
        List<OrderItem> items,
        BigDecimal totalPrice,
        String status,
        LocalDateTime orderDate
) {
    public record OrderItem(
            Integer orderItemId,
            Integer menuId,
            Integer quantity,
            BigDecimal unitPrice
    ) {
    }
}
