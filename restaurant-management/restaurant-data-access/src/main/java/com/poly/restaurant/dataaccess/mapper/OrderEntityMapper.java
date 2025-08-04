package com.poly.restaurant.dataaccess.mapper;

import com.poly.restaurant.dataaccess.entity.OrderItemJpaEntity;
import com.poly.restaurant.dataaccess.entity.OrderJpaEntity;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderItem;
import com.poly.restaurant.domain.entity.OrderStatus;

import java.util.List;
import java.util.stream.Collectors;

public class OrderEntityMapper {

    // Domain -> JPA: OrderItem -> OrderItemJpaEntity
    public static OrderItemJpaEntity toEntity(OrderItem domainItem, OrderJpaEntity order) {
        return OrderItemJpaEntity.builder()
                .order(order)
                .menuItemId(domainItem.getMenuItemId())
                .quantity(domainItem.getQuantity())
                .price(domainItem.getPrice())
                .build();
    }

    // Domain -> JPA: Order -> OrderJpaEntity
    public static OrderJpaEntity toEntity(Order domainOrder) {
        OrderJpaEntity orderEntity = OrderJpaEntity.builder()
                .id(domainOrder.getId())
                .customerId(domainOrder.getCustomerId())
                .tableId(domainOrder.getTableId())
                .createdAt(domainOrder.getCreatedAt())
                .status(domainOrder.getStatus())
                .customerNote(domainOrder.getCustomerNote())
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
                jpaEntity.getMenuItemId(),
                jpaEntity.getQuantity(),
                jpaEntity.getPrice()
        );
    }

    // JPA -> Domain: OrderJpaEntity -> Order
    public static Order toDomain(OrderJpaEntity jpaEntity) {
        List<OrderItem> domainItems = jpaEntity.getItems().stream()
                .map(OrderEntityMapper::toDomain)
                .collect(Collectors.toList());

        return new Order(
                jpaEntity.getId(),
                jpaEntity.getCustomerId(),
                jpaEntity.getTableId(),
                domainItems,
                jpaEntity.getCreatedAt()
        );
    }
}
