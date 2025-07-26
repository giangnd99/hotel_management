package com.poly.booking.management.domain.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.OrderId;
import com.poly.domain.valueobject.OrderStatus;

public class Order extends BaseEntity<OrderId> {

    private Money totalCost;
    private OrderStatus status;

    public Order(OrderId orderId, Money totalCost, OrderStatus status) {
        super.setId(orderId);
        this.totalCost = totalCost;
        this.status = status;
    }

    public Money getTotalCost() {
        return totalCost;
    }

    public OrderStatus getStatus() {
        return status;
    }

}
