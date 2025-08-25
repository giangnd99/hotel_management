package com.poly.restaurant.dataaccess.mapper;

import com.poly.restaurant.dataaccess.entity.MenuItemJpaEntity;
import com.poly.restaurant.dataaccess.entity.OrderItemJpaEntity;
import com.poly.restaurant.dataaccess.entity.OrderJpaEntity;
import com.poly.restaurant.dataaccess.jpa.JpaMenuItemRepository;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderEntityMapper {

    public static OrderItemJpaEntity toEntity(OrderItem domainItem, OrderJpaEntity order, JpaMenuItemRepository menuItemRepository) {
        BigDecimal unit = domainItem.getPrice();
        BigDecimal total = unit.multiply(BigDecimal.valueOf(domainItem.getQuantity()));
        
        // Fetch the menu item entity
        MenuItemJpaEntity menuItem = menuItemRepository.findById(domainItem.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found: " + domainItem.getMenuItemId()));
        
        return OrderItemJpaEntity.builder()
                .id("OI" + String.format("%03d", System.currentTimeMillis() % 1000)) // Generate simple ID like OI001
                .order(order)
                .menuItem(menuItem)
                .quantity(domainItem.getQuantity())
                .unitPrice(unit)
                .totalPrice(total)
                .specialInstructions(null)
                .createdAt(LocalDateTime.now()) // Set current timestamp
                .build();
    }

    public static OrderJpaEntity toEntity(Order domainOrder, JpaMenuItemRepository menuItemRepository) {
        // Calculate total amount from order items
        BigDecimal totalAmount = domainOrder.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderJpaEntity orderEntity = OrderJpaEntity.builder()
                .id(domainOrder.getId())
                .customerId(domainOrder.getCustomerId())
                .roomId(domainOrder.getTableId())
                .orderNumber(domainOrder.getOrderNumber())
                .totalAmount(totalAmount)
                .createdAt(domainOrder.getCreatedAt())
                .status(domainOrder.getStatus())
                .paymentStatus("PENDING") // Default payment status
                .orderType("DINE_IN") // Default order type
                .specialInstructions(domainOrder.getCustomerNote())
                .updatedAt(LocalDateTime.now()) // Set current timestamp
                .build();

        List<OrderItemJpaEntity> jpaItems = domainOrder.getItems().stream()
                .map(item -> toEntity(item, orderEntity, menuItemRepository))
                .collect(Collectors.toList());
        orderEntity.setItems(jpaItems);
        return orderEntity;
    }

    public static OrderItem toDomain(OrderItemJpaEntity jpaEntity) {
        return new OrderItem(
                jpaEntity.getMenuItem().getId(),
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
                jpaEntity.getCreatedAt(),
                jpaEntity.getOrderNumber(), // Pass orderNumber to constructor
                jpaEntity.getStatus() // Pass status directly to constructor
        );
        order.setCustomerNote(jpaEntity.getSpecialInstructions());
        
        return order;
    }
}
