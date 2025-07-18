package com.poly.restaurant.dataaccess.mapper;

import com.poly.restaurant.dataaccess.entity.OrderItemJpaEntity;
import com.poly.restaurant.dataaccess.entity.OrderJpaEntity;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderItem;
import com.poly.restaurant.domain.value_object.MenuItemId;
import com.poly.restaurant.domain.value_object.OrderId;

import java.util.List;
import java.util.stream.Collectors;

public class OrderEntityMapper {

    // Domain -> JPA: OrderItem -> OrderJpaEntity
    public static OrderJpaEntity toEntity(OrderItem domainItem) {
        return OrderJpaEntity.builder()
                .orderItemId(domainItem.getOrderItemId())
                .menuItemId(domainItem.getMenuItemId().getValue()) // unwrap value object
                .quantity(domainItem.getQuantity())
                .unitPrice(domainItem.getUnitPrice())
                .build();
    }

    // Domain -> JPA: Order -> OrderItemJpaEntity
    public static OrderItemJpaEntity toEntity(Order domainOrder) {
        List<OrderJpaEntity> jpaItems = domainOrder.getItems().stream()
                .map(OrderEntityMapper::toEntity)
                .collect(Collectors.toList());

        return OrderItemJpaEntity.builder()
                .orderId(domainOrder.getOrderId().getValue())
                .customerId(domainOrder.getCustomerId())
                .orderDate(domainOrder.getOrderDate())
                .items(jpaItems)
                .totalPrice(domainOrder.getTotalPrice())
                .status(domainOrder.getStatus())
                .build();
    }

    // JPA -> Domain: OrderJpaEntity -> OrderItem
    public static OrderItem toDomain(OrderJpaEntity jpaEntity) {
        return new OrderItem(
                jpaEntity.getOrderItemId(),
                new MenuItemId(jpaEntity.getMenuItemId()),
                jpaEntity.getQuantity(),
                jpaEntity.getUnitPrice()
        );
    }

    // JPA -> Domain: OrderItemJpaEntity -> Order
    public static Order toDomain(OrderItemJpaEntity jpaEntity) {
        List<OrderItem> domainItems = jpaEntity.getItems().stream()
                .map(OrderEntityMapper::toDomain)
                .collect(Collectors.toList());

        return new Order(
                new OrderId(jpaEntity.getOrderId()),
                jpaEntity.getCustomerId(),
                jpaEntity.getOrderDate(),
                domainItems,
                jpaEntity.getTotalPrice(),
                jpaEntity.getStatus()
        );
    }
}
