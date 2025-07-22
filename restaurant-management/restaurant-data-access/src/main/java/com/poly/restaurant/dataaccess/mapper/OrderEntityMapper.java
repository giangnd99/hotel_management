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

    // Domain -> JPA: OrderItem -> OrderItemJpaEntity
    public static OrderItemJpaEntity toEntity(OrderItem domainItem, OrderJpaEntity order) {
        return OrderItemJpaEntity.builder()
                .orderItemId(domainItem.getOrderItemId())
                .order(order)
                .menuItemId(domainItem.getMenuItemId().getValue())
                .quantity(domainItem.getQuantity())
                .unitPrice(domainItem.getUnitPrice())
                .build();
    }

    // Domain -> JPA: Order -> OrderJpaEntity
    public static OrderJpaEntity toEntity(Order domainOrder) {
        OrderJpaEntity orderEntity = OrderJpaEntity.builder()
                .orderId(domainOrder.getOrderId().getValue())
                .customerId(domainOrder.getCustomerId())
                .orderDate(domainOrder.getOrderDate())
                .totalPrice(domainOrder.getTotalPrice())
                .status(domainOrder.getStatus())
                .build();

        // map items kèm order reference
        List<OrderItemJpaEntity> jpaItems = domainOrder.getItems().stream()
                .map(item -> toEntity(item, orderEntity))
                .collect(Collectors.toList());

        orderEntity.setItems(jpaItems);

        return orderEntity;
    }

    // JPA -> Domain: OrderItemJpaEntity -> OrderItem
    public static OrderItem toDomain(OrderItemJpaEntity jpaEntity) {
        return new OrderItem(
                jpaEntity.getOrderItemId(),
                new MenuItemId(jpaEntity.getMenuItemId()),
                jpaEntity.getQuantity(),
                jpaEntity.getUnitPrice()
        );
    }

    // JPA -> Domain: OrderJpaEntity -> Order
    public static Order toDomain(OrderJpaEntity jpaEntity) {
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
