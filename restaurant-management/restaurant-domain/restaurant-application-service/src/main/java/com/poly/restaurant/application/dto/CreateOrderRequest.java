package com.poly.restaurant.application.dto;

import java.util.List;

public record CreateOrderRequest(Integer tableId, List<OrderItem> items) {
    public record OrderItem(Integer menuId, Integer quantity) {
    }
}
