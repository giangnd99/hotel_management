package com.poly.restaurant.application.port.in.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.*;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.mapper.OrderMapper;
import com.poly.restaurant.application.port.in.RestaurantUseCase;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@DomainHandler
@RequiredArgsConstructor
@Slf4j
public class RestaurantUseCaseImpl implements RestaurantUseCase {

    private final OrderHandler orderHandler;

    @Override
    public List<TableDTO> getAllTables() {
        log.info("Getting all tables");
        return List.of();
    }

    @Override
    public OrderDTO createOrder(OrderDTO request) {
        log.info("Creating new order: {}", request.id());
        Order order = OrderMapper.toEntity(request);
        Order saved = orderHandler.createOrder(order);
        return OrderMapper.toDto(saved);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        log.info("Getting all orders");
        return orderHandler.getAll().stream().map(OrderMapper::toDto).toList();
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        log.info("Getting all staff");
        return List.of();
    }

    // Thêm các method mới để sử dụng OrderHandler
    public OrderDTO updateOrderStatus(String orderId, String status) {
        log.info("Updating order status: {} to {}", orderId, status);
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        Order updated = orderHandler.updateOrderStatus(orderId, orderStatus);
        return OrderMapper.toDto(updated);
    }

    public OrderDTO cancelOrder(String orderId) {
        log.info("Cancelling order: {}", orderId);
        Order cancelled = orderHandler.cancelOrder(orderId);
        return OrderMapper.toDto(cancelled);
    }

    public List<OrderDTO> getOrdersByCustomer(String customerId) {
        log.info("Getting orders by customer: {}", customerId);
        return orderHandler.getOrdersByCustomer(customerId).stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    public List<OrderDTO> getOrdersByTable(String tableId) {
        log.info("Getting orders by table: {}", tableId);
        return orderHandler.getOrdersByTable(tableId).stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        log.info("Getting orders by status: {}", status);
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return orderHandler.getOrdersByStatus(orderStatus).stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    public OrderDTO processOrder(String orderId) {
        log.info("Processing order: {}", orderId);
        Order processed = orderHandler.processOrder(orderId);
        return OrderMapper.toDto(processed);
    }

    public OrderDTO completeOrder(String orderId) {
        log.info("Completing order: {}", orderId);
        Order completed = orderHandler.completeOrder(orderId);
        return OrderMapper.toDto(completed);
    }
}
