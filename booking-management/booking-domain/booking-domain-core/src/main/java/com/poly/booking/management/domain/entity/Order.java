package com.poly.booking.management.domain.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.OrderId;
import com.poly.domain.valueobject.OrderStatus;

public class Order extends BaseEntity<OrderId> {

    private Money totalCost;
    private OrderStatus status;

}
