package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.port.out.OrderRepositoryPort;
import com.poly.restaurant.application.port.out.RepositoryPort;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@DomainHandler
@RequiredArgsConstructor
@Slf4j
public class OrderHandlerImpl extends AbstractGenericHandlerImpl<Order, String> implements OrderHandler {

    private final OrderRepositoryPort repository;

    @Override
    protected RepositoryPort<Order, String> getRepository() {
        return repository;
    }

    @Override
    public Order createOrder(Order order) {
        log.info("Creating new order: {}", order.getId());
        // Validation logic
        return repository.save(order);
    }

    @Override
    public Order updateOrderStatus(String orderId, OrderStatus newStatus) {
        log.info("Updating order status: {} to {}", orderId, newStatus);
        Order order = getById(orderId);
        order.setStatus(newStatus);
        return repository.save(order);
    }

    @Override
    public Order cancelOrder(String orderId) {
        log.info("Cancelling order: {}", orderId);
        Order order = getById(orderId);
        
        if (order.isCompleted()) {
            throw new IllegalStateException("Cannot cancel completed order: " + orderId);
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        return repository.save(order);
    }

    @Override
    public List<Order> getOrdersByCustomer(String customerId) {
        log.info("Getting orders by customer: {}", customerId);
        return repository.findAll().stream()
                .filter(order -> order.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getOrdersByTable(String tableId) {
        log.info("Getting orders by table: {}", tableId);
        return repository.findAll().stream()
                .filter(order -> order.getTableId().equals(tableId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        log.info("Getting orders by status: {}", status);
        return repository.findAll().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Order processOrder(String orderId) {
        log.info("Processing order: {}", orderId);
        Order order = getById(orderId);
        
        if (order.getStatus() != OrderStatus.NEW) {
            throw new IllegalStateException("Order must be in NEW status to be processed: " + orderId);
        }
        
        order.setStatus(OrderStatus.IN_PROGRESS);
        return repository.save(order);
    }

    @Override
    public Order completeOrder(String orderId) {
        log.info("Completing order: {}", orderId);
        Order order = getById(orderId);
        
        if (order.getStatus() != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Order must be in IN_PROGRESS status to be completed: " + orderId);
        }
        
        order.setStatus(OrderStatus.COMPLETED);
        return repository.save(order);
    }
}
