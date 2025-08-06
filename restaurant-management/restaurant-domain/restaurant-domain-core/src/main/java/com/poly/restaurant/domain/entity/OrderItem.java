package com.poly.restaurant.domain.entity;

import java.math.BigDecimal;

public class OrderItem {
    private final String menuItemId;
    private final int quantity;
    private final BigDecimal price;

    public OrderItem(String menuItemId, int quantity, BigDecimal price) {
        if (menuItemId == null || menuItemId.isEmpty()) throw new IllegalArgumentException("MenuItemId must not be empty");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Price must be non-negative");
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getMenuItemId() { return menuItemId; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
}
