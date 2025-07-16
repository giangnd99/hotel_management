package com.poly.restaurant.application.entity;

import com.poly.restaurant.application.value_object.OrderId;
import com.poly.restaurant.application.value_object.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private final OrderId orderId;
    private final int customerId;
    private final LocalDateTime orderDate;
    private final List<OrderItem> items;
    private final BigDecimal totalPrice;
    private final OrderStatus status;

    public Order(OrderId orderId, int customerId, LocalDateTime orderDate,
                 List<OrderItem> items, BigDecimal totalPrice, OrderStatus status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
