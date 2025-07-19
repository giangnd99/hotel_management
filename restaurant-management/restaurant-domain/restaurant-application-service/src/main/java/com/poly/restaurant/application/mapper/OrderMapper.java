package com.poly.restaurant.application.mapper;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderItem;
import com.poly.restaurant.domain.value_object.MenuItemId;
import com.poly.restaurant.domain.value_object.OrderId;
import com.poly.restaurant.domain.value_object.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    // DTO -> Domain: OrderDTO -> Order
    public static Order toEntity(OrderDTO dto) {
        if (dto == null) return null;

        List<OrderItem> items = dto.items().stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());

        return new Order(
                new OrderId(dto.orderId()),
                dto.tableId(),
                dto.orderDate() != null ? dto.orderDate() : LocalDateTime.now(),
                items,
                dto.totalPrice() != null ? dto.totalPrice() : calculateTotalPrice(items),
                OrderStatus.valueOf(dto.status())
        );
    }

    public static OrderItem toEntity(OrderDTO.OrderItem dtoItem) {
        return new OrderItem(
                dtoItem.orderItemId() != null ? dtoItem.orderItemId() : 0,
                new MenuItemId(dtoItem.menuId()),
                dtoItem.quantity(),
                dtoItem.unitPrice()
        );
    }

    // Domain -> DTO: Order -> OrderDTO
    public static OrderDTO toDto(Order order) {
        List<OrderDTO.OrderItem> dtoItems = order.getItems().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getOrderId().getValue(),
                order.getCustomerId(),
                dtoItems,
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getOrderDate()
        );
    }

    public static OrderDTO.OrderItem toDto(OrderItem item) {
        return new OrderDTO.OrderItem(
                item.getOrderItemId(),
                item.getMenuItemId().getValue(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }

    private static BigDecimal calculateTotalPrice(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
