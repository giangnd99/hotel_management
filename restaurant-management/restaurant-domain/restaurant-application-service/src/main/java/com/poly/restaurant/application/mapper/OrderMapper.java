package com.poly.restaurant.application.mapper;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderItem;

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
                dto.id(),
                dto.customerId(),
                dto.tableId(),
                items,
                dto.createdAt() != null ? dto.createdAt() : LocalDateTime.now()
        );
    }

    public static OrderItem toEntity(OrderDTO.OrderItem dtoItem) {
        return new OrderItem(
                dtoItem.menuItemId(),
                dtoItem.quantity(),
                dtoItem.price()
        );
    }

    // Domain -> DTO: Order -> OrderDTO
    public static OrderDTO toDto(Order order) {
        List<OrderDTO.OrderItem> dtoItems = order.getItems().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getCustomerId(),
                order.getTableId(),
                dtoItems,
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getCustomerNote()
        );
    }

    public static OrderDTO.OrderItem toDto(OrderItem item) {
        return new OrderDTO.OrderItem(
                item.getMenuItemId(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}
