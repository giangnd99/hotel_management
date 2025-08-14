package com.poly.restaurant.application.port.in.impl;

import com.poly.restaurant.application.port.in.OrderUseCase;
import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.handler.conmand.CreateOrderDirectlyCommand;
import com.poly.restaurant.application.handler.conmand.CreateOrderWithRoomDetailCommand;
import com.poly.restaurant.application.mapper.OrderMapper;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@DomainHandler
@RequiredArgsConstructor
@Slf4j
public class OrderUseCaseImpl implements OrderUseCase {

    private final OrderHandler orderHandler;
    private final CreateOrderDirectlyCommand createOrderDirectlyCommand;
    private final CreateOrderWithRoomDetailCommand createOrderWithRoomDetailCommand;

    // ========== BASIC CRUD OPERATIONS ==========

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
        return orderHandler.getAll().stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDTO getOrderById(String id) {
        log.info("Getting order by id: {}", id);
        Order order = orderHandler.getById(id);
        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDTO updateOrder(String id, OrderDTO request) {
        log.info("Updating order: {}", id);
        Order order = OrderMapper.toEntity(request);
        Order updated = orderHandler.update(id, order);
        return OrderMapper.toDto(updated);
    }

    @Override
    public void deleteOrder(String id) {
        log.info("Deleting order: {}", id);
        orderHandler.delete(id);
    }

    // ========== BUSINESS OPERATIONS ==========

    @Override
    public OrderDTO createOrderWithPayment(OrderDTO orderDTO) {
        log.info("Creating order with payment: {}", orderDTO.id());
        return orderHandler.createOrderWithPayment(orderDTO);
    }

    @Override
    public OrderDTO updateOrderStatus(String orderId, String status) {
        log.info("Updating order status: {} to {}", orderId, status);
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        Order updated = orderHandler.updateOrderStatus(orderId, orderStatus);
        return OrderMapper.toDto(updated);
    }

    @Override
    public OrderDTO cancelOrder(String orderId) {
        log.info("Cancelling order: {}", orderId);
        Order cancelled = orderHandler.cancelOrder(orderId);
        return OrderMapper.toDto(cancelled);
    }

    @Override
    public OrderDTO processOrder(String orderId) {
        log.info("Processing order: {}", orderId);
        Order processed = orderHandler.processOrder(orderId);
        return OrderMapper.toDto(processed);
    }

    @Override
    public OrderDTO completeOrder(String orderId) {
        log.info("Completing order: {}", orderId);
        Order completed = orderHandler.completeOrder(orderId);
        return OrderMapper.toDto(completed);
    }

    @Override
    public OrderDTO processOrderWithNotification(String orderId) {
        log.info("Processing order with notification: {}", orderId);
        return orderHandler.processOrderWithNotification(orderId);
    }

    @Override
    public OrderDTO completeOrderWithNotification(String orderId) {
        log.info("Completing order with notification: {}", orderId);
        return orderHandler.completeOrderWithNotification(orderId);
    }

    @Override
    public OrderDTO cancelOrderWithRefundAndNotification(String orderId, String reason) {
        log.info("Cancelling order with refund and notification: {}", orderId);
        return orderHandler.cancelOrderWithRefundAndNotification(orderId, reason);
    }

    // ========== QUERY OPERATIONS ==========

    @Override
    public List<OrderDTO> getOrdersByCustomer(String customerId) {
        log.info("Getting orders by customer: {}", customerId);
        return orderHandler.getOrdersByCustomer(customerId).stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderDTO> getOrdersByTable(String tableId) {
        log.info("Getting orders by table: {}", tableId);
        return orderHandler.getOrdersByTable(tableId).stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderDTO> getOrdersByStatus(String status) {
        log.info("Getting orders by status: {}", status);
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return orderHandler.getOrdersByStatus(orderStatus).stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    // ========== NEW ORDER TYPES ==========

    @Override
    public OrderDTO createDirectOrder(OrderDTO orderDTO) {
        log.info("Creating direct order: {}", orderDTO.id());
        return createOrderDirectlyCommand.createOrder(orderDTO);
    }

    @Override
    public OrderDTO createRoomAttachedOrder(OrderDTO orderDTO) {
        log.info("Creating room attached order: {}", orderDTO.id());
        return createOrderWithRoomDetailCommand.createOrderWithRoomDetail(orderDTO);
    }

    @Override
    public void triggerDirectPaymentRequest(OrderDTO orderDTO) {
        log.info("Triggering direct payment request for order: {}", orderDTO.id());
        createOrderDirectlyCommand.triggerPaymentRequest(orderDTO);
    }

    @Override
    public void triggerRoomOrderPaymentRequest(OrderDTO orderDTO) {
        log.info("Triggering room order payment request for order: {}", orderDTO.id());
        createOrderWithRoomDetailCommand.triggerPaymentRequest(orderDTO);
    }
}