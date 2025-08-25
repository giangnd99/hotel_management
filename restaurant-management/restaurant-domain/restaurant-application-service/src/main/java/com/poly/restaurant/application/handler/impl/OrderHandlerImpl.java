package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.handler.MenuItemHandler;
import com.poly.restaurant.application.mapper.OrderMapper;
import com.poly.restaurant.application.port.out.OrderRepositoryPort;
import com.poly.restaurant.application.port.out.RepositoryPort;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderItem;
import com.poly.restaurant.domain.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@DomainHandler
@RequiredArgsConstructor
@Slf4j
public class OrderHandlerImpl extends AbstractGenericHandlerImpl<Order, String> implements OrderHandler {

    private final OrderRepositoryPort repository;
    private final MenuItemHandler menuItemHandler;

    @Override
    protected RepositoryPort<Order, String> getRepository() {
        return repository;
    }

    @Override
    public Order createOrder(Order order) {
        log.info("Creating new order: {}", order.getId());
        
        // Validate order
        validateOrder(order);
        
        // Set customer note if provided
        if (order.getCustomerNote() != null) {
            order.setCustomerNote(order.getCustomerNote());
        }
        
        // Only set status to NEW if it's not already NEW (to avoid transition error)
        if (order.getStatus() != OrderStatus.NEW) {
            order.setStatus(OrderStatus.NEW);
        }
        
        // Save order
        Order savedOrder = repository.save(order);
        
        log.info("Order created successfully: {} with status: {}", savedOrder.getId(), savedOrder.getStatus());
        return savedOrder;
    }

    private void validateOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        
        if (order.getId() == null || order.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty");
        }
        
        if (order.getCustomerId() == null || order.getCustomerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        
        if (order.getTableId() == null || order.getTableId().trim().isEmpty()) {
            throw new IllegalArgumentException("Table ID cannot be null or empty");
        }
        
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        
        // Validate each order item
        for (OrderItem item : order.getItems()) {
            if (item.getMenuItemId() == null || item.getMenuItemId().trim().isEmpty()) {
                throw new IllegalArgumentException("Menu item ID cannot be null or empty");
            }
            
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            
            if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be greater than 0");
            }
        }
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
        return repository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> getOrdersByTable(String tableId) {
        log.info("Getting orders by table: {}", tableId);
        return repository.findByTableId(tableId);
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus status) {
        log.info("Getting orders by status: {}", status);
        return repository.findByStatus(status);
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

    @Override
    public OrderDTO createOrderWithPayment(OrderDTO orderDTO) {
        log.info("Creating order with payment: {}", orderDTO.id());

        // 1. Validate menu items availability
        validateMenuItemsAvailability(orderDTO);

        // 2. Create order
        Order order = createOrder(OrderMapper.toEntity(orderDTO));

        // 3. Update order status to IN_PROGRESS (simulating successful payment)
        order = updateOrderStatus(order.getId(), OrderStatus.IN_PROGRESS);

        log.info("Order created successfully with payment simulation: {}", order.getId());
        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDTO processOrderWithNotification(String orderId) {
        log.info("Processing order with notification: {}", orderId);

        Order order = processOrder(orderId);
        log.info("Order processed successfully: {}", orderId);

        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDTO completeOrderWithNotification(String orderId) {
        log.info("Completing order with notification: {}", orderId);

        Order order = completeOrder(orderId);
        log.info("Order completed successfully: {}", orderId);

        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDTO cancelOrderWithRefundAndNotification(String orderId, String reason) {
        log.info("Cancelling order with refund and notification: {}", orderId);

        Order order = cancelOrder(orderId);
        log.info("Order cancelled successfully with reason: {}", reason);

        return OrderMapper.toDto(order);
    }

    private void validateMenuItemsAvailability(OrderDTO orderDTO) {
        for (OrderDTO.OrderItem item : orderDTO.items()) {
            if (!menuItemHandler.isItemAvailable(item.menuItemId())) {
                throw new IllegalStateException("Menu item not available: " + item.menuItemId());
            }
        }
    }

    private BigDecimal calculateTotalAmount(Order order) {
        return order.getItems().stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
