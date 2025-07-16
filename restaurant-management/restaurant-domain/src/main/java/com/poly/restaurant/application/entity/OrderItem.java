package com.poly.restaurant.application.entity;

import com.poly.restaurant.application.value_object.MenuItemId;

import java.math.BigDecimal;

public class OrderItem {
    private final int orderItemId;
    private final MenuItemId menuItemId;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderItem(int orderItemId, MenuItemId menuItemId, int quantity, BigDecimal unitPrice) {
        this.orderItemId = orderItemId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public MenuItemId getMenuItemId() {
        return menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
