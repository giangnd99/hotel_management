package com.poly.restaurant.application.dto;

import java.util.List;

public record OrderDTO(Integer orderId, Integer tableId, List<OrderItem> items, String status) {
    public record OrderItem(Integer menuId, Integer quantity) {
    }
}
