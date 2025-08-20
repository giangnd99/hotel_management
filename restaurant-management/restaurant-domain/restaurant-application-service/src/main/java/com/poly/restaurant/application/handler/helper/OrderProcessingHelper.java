package com.poly.restaurant.application.handler.helper;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingHelper {

    private final OrderHandler orderHandler;

    public OrderDTO createDirectOrder(OrderDTO orderDTO) {
        log.info("Creating direct order: {}", orderDTO.id());
        validateOrder(orderDTO);
        Order order = createOrderEntity(orderDTO);
        Order savedOrder = orderHandler.createOrder(order);
        return convertToDTO(savedOrder);
    }

    public OrderDTO createRoomAttachedOrder(OrderDTO orderDTO, String roomId) {
        log.info("Creating room attached order: {} for room: {}", orderDTO.id(), roomId);
        validateOrder(orderDTO);
        validateRoomId(roomId);
        Order order = createOrderEntity(orderDTO);
        Order savedOrder = orderHandler.createOrder(order);
        return convertToDTO(savedOrder);
    }

    public void triggerDirectPaymentRequest(OrderDTO orderDTO) {
        log.info("Simulating direct payment trigger for order: {} (messaging disabled)", orderDTO.id());
        orderHandler.updateOrderStatus(orderDTO.id(), OrderStatus.IN_PROGRESS);
    }

    public void triggerRoomOrderRequest(OrderDTO orderDTO, String roomId) {
        log.info("Simulating room order attach for order: {} to room: {} (messaging disabled)", orderDTO.id(), roomId);
        orderHandler.updateOrderStatus(orderDTO.id(), OrderStatus.NEW);
    }

    public void triggerRoomOrderPaymentRequest(OrderDTO orderDTO, String roomId) {
        log.info("Simulating room order payment for order: {} from room: {} (messaging disabled)", orderDTO.id(), roomId);
        // No-op while messaging disabled
    }

    private void validateOrder(OrderDTO orderDTO) {
        if (orderDTO == null) {
            throw new IllegalArgumentException("OrderDTO cannot be null");
        }
        if (orderDTO.id() == null || orderDTO.id().trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        if (orderDTO.customerId() == null || orderDTO.customerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        if (orderDTO.tableId() == null || orderDTO.tableId().trim().isEmpty()) {
            throw new IllegalArgumentException("Table ID cannot be null or empty");
        }
        if (orderDTO.items() == null || orderDTO.items().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
    }

    private void validateRoomId(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
    }

    private Order createOrderEntity(OrderDTO orderDTO) {
        return new Order(
                orderDTO.id(),
                orderDTO.customerId(),
                orderDTO.tableId(),
                new ArrayList<>(),
                java.time.LocalDateTime.now()
        );
    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getCustomerId(),
                order.getTableId(),
                new ArrayList<>(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getCustomerNote()
        );
    }

    private BigDecimal calculateTotalAmount(OrderDTO orderDTO) {
        return orderDTO.items().stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
