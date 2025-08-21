package com.poly.restaurant.dataaccess.mapper;

import com.poly.restaurant.dataaccess.entity.OrderItemJpaEntity;
import com.poly.restaurant.dataaccess.entity.OrderJpaEntity;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderEntityMapper {

    public static OrderItemJpaEntity toEntity(OrderItem domainItem, OrderJpaEntity order) {
        BigDecimal unit = domainItem.getPrice();
        BigDecimal total = unit.multiply(BigDecimal.valueOf(domainItem.getQuantity()));
        return OrderItemJpaEntity.builder()
                .id(UUID.randomUUID().toString())
                .order(order)
                .menuItemId(domainItem.getMenuItemId())
                .quantity(domainItem.getQuantity())
                .unitPrice(unit)
                .totalPrice(total)
                .build();
    }

    public static OrderJpaEntity toEntity(Order domainOrder) {
        OrderJpaEntity orderEntity = OrderJpaEntity.builder()
                .id(domainOrder.getId())
                .customerId(domainOrder.getCustomerId())
                .roomId(domainOrder.getTableId())
                .createdAt(domainOrder.getCreatedAt())
                .status(domainOrder.getStatus())
                .specialInstructions(domainOrder.getCustomerNote())
                .build();

        List<OrderItemJpaEntity> jpaItems = domainOrder.getItems().stream()
                .map(item -> toEntity(item, orderEntity))
                .collect(Collectors.toList());
        orderEntity.setItems(jpaItems);
        return orderEntity;
    }

    public static OrderItem toDomain(OrderItemJpaEntity jpaEntity) {
        return new OrderItem(
                jpaEntity.getMenuItemId(),
                jpaEntity.getQuantity(),
                jpaEntity.getUnitPrice()
        );
    }

    public static Order toDomain(OrderJpaEntity jpaEntity) {
        List<OrderItem> domainItems = jpaEntity.getItems() == null ? List.of()
                : jpaEntity.getItems().stream().map(OrderEntityMapper::toDomain).collect(Collectors.toList());

        Order order = new Order(
                jpaEntity.getId(),
                jpaEntity.getCustomerId(),
                jpaEntity.getRoomId(),
                domainItems,
                jpaEntity.getCreatedAt()
        );
        order.setCustomerNote(jpaEntity.getSpecialInstructions());
        return order;
    }
}
