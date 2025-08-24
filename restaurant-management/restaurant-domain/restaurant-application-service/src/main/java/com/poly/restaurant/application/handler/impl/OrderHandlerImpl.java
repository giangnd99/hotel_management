package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.dto.CustomerDTO;
import com.poly.restaurant.application.dto.NotificationRequestDTO;
import com.poly.restaurant.application.dto.PaymentRequestDTO;
import com.poly.restaurant.application.dto.PaymentResponseDTO;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.handler.MenuItemHandler;
import com.poly.restaurant.application.mapper.OrderMapper;
import com.poly.restaurant.application.port.out.repo.OrderRepositoryPort;
import com.poly.restaurant.application.port.out.repo.RepositoryPort;
import com.poly.restaurant.application.port.out.feign.PaymentFeignClient;
import com.poly.restaurant.application.port.out.feign.CustomerFeignClient;
import com.poly.restaurant.application.port.out.feign.NotificationFeignClient;
import com.poly.restaurant.domain.entity.Order;
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
    private final PaymentFeignClient paymentFeignClient;
    private final CustomerFeignClient customerFeignClient;
    private final NotificationFeignClient notificationFeignClient;

    @Override
    protected RepositoryPort<Order, String> getRepository() {
        return repository;
    }

    @Override
    public Order createOrder(Order order) {
        log.info("Creating new order: {}", order.getId());
        // Validation orderItems
        //
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

    @Override
    public OrderDTO createOrderWithPayment(OrderDTO orderDTO) {
        log.info("Creating order with payment: {}", orderDTO.id());

        // 1. Validate customer
        CustomerDTO customer = validateCustomer(orderDTO.customerId());

        // 2. Validate menu items availability
        validateMenuItemsAvailability(orderDTO);

        // 3. Create order
        Order order = createOrder(OrderMapper.toEntity(orderDTO));

        // 4. Process payment
        PaymentResponseDTO paymentResponse = processPayment(order, customer);

        // 5. Update order status based on payment
        if ("SUCCESS".equals(paymentResponse.status())) {
            order = updateOrderStatus(order.getId(), OrderStatus.IN_PROGRESS);
        } else {
            order = updateOrderStatus(order.getId(), OrderStatus.CANCELLED);
        }

        // 6. Send notification
        sendOrderNotification(order, customer, paymentResponse);

        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDTO processOrderWithNotification(String orderId) {
        log.info("Processing order with notification: {}", orderId);

        Order order = processOrder(orderId);

        // Send notification
        CustomerDTO customer = customerFeignClient.getCustomerById(order.getCustomerId());
        sendOrderStatusNotification(order, customer, "IN_PROGRESS", "Đơn hàng đang được xử lý");

        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDTO completeOrderWithNotification(String orderId) {
        log.info("Completing order with notification: {}", orderId);

        Order order = completeOrder(orderId);

        // Send notification
        CustomerDTO customer = customerFeignClient.getCustomerById(order.getCustomerId());
        sendOrderStatusNotification(order, customer, "COMPLETED", "Đơn hàng đã hoàn thành");

        return OrderMapper.toDto(order);
    }

    @Override
    public OrderDTO cancelOrderWithRefundAndNotification(String orderId, String reason) {
        log.info("Cancelling order with refund and notification: {}", orderId);

        Order order = cancelOrder(orderId);

        // Process refund if payment was successful
        if (order.getStatus() == OrderStatus.CANCELLED) {
            processRefund(order, reason);
        }

        // Send notification
        CustomerDTO customer = customerFeignClient.getCustomerById(order.getCustomerId());
        sendOrderStatusNotification(order, customer, "CANCELLED", "Đơn hàng đã bị hủy: " + reason);

        return OrderMapper.toDto(order);
    }

    private CustomerDTO validateCustomer(String customerId) {
        try {
            CustomerDTO customer = customerFeignClient.getCustomerById(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found: " + customerId);
            }
            return customer;
        } catch (Exception e) {
            log.error("Error validating customer: {}", customerId, e);
            throw new RuntimeException("Failed to validate customer: " + customerId);
        }
    }

    private void validateMenuItemsAvailability(OrderDTO orderDTO) {
        for (OrderDTO.OrderItem item : orderDTO.items()) {
            if (!menuItemHandler.isItemAvailable(Integer.parseInt(item.menuItemId()))) {
                throw new IllegalStateException("Menu item not available: " + item.menuItemId());
            }

            if (!menuItemHandler.hasSufficientQuantity(Integer.parseInt(item.menuItemId()), item.quantity())) {
                throw new IllegalStateException("Insufficient quantity for menu item: " + item.menuItemId());
            }
        }
    }

    private PaymentResponseDTO processPayment(Order order, CustomerDTO customer) {
        try {
            BigDecimal totalAmount = calculateTotalAmount(order);

            PaymentRequestDTO paymentRequest = new PaymentRequestDTO(
                order.getId(),
                order.getCustomerId(),
                totalAmount,
                "CASH", // Default payment method
                "Restaurant order payment"
            );

            return paymentFeignClient.processPayment(paymentRequest);
        } catch (Exception e) {
            log.error("Error processing payment for order: {}", order.getId(), e);
            throw new RuntimeException("Failed to process payment");
        }
    }

    private void processRefund(Order order, String reason) {
        try {
            paymentFeignClient.refundPayment(order.getId(), reason);
        } catch (Exception e) {
            log.error("Error processing refund for order: {}", order.getId(), e);
        }
    }

    private BigDecimal calculateTotalAmount(Order order) {
        return order.getItems().stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void sendOrderNotification(Order order, CustomerDTO customer, PaymentResponseDTO paymentResponse) {
        try {
            NotificationRequestDTO notificationRequest = new NotificationRequestDTO(
                customer.getCustomerId().toString(),
                "ORDER_CREATED",
                "Đơn hàng mới",
                String.format("Đơn hàng %s đã được tạo. Trạng thái thanh toán: %s",
                    order.getId(), paymentResponse.status()),
                order.getId()
            );

            notificationFeignClient.sendNotification(notificationRequest);
        } catch (Exception e) {
            log.error("Error sending order notification: {}", order.getId(), e);
        }
    }

    private void sendOrderStatusNotification(Order order, CustomerDTO customer, String status, String message) {
        try {
            NotificationRequestDTO notificationRequest = new NotificationRequestDTO(
                customer.getCustomerId().toString(),
                "ORDER_STATUS_UPDATE",
                "Cập nhật đơn hàng",
                String.format("Đơn hàng %s: %s", order.getId(), message),
                order.getId()
            );

            notificationFeignClient.sendOrderStatusNotification(notificationRequest);
        } catch (Exception e) {
            log.error("Error sending order status notification: {}", order.getId(), e);
        }
    }
}
