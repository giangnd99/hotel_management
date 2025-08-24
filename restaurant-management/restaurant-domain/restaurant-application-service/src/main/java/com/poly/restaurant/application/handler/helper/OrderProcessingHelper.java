package com.poly.restaurant.application.handler.helper;

import com.poly.message.model.payment.PaymentRequestMessage;
import com.poly.message.model.room.RoomRequestMessage;
import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.port.out.message.publisher.PaymentRequestPublisher;
import com.poly.restaurant.application.port.out.message.publisher.RoomRequestPublisher;
import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Helper class cho order processing logic
 * <p>
 * NGHIỆP VỤ:
 * - Xử lý logic tạo order trực tiếp
 * - Xử lý logic tạo order đính kèm room
 * - Tính toán tổng tiền
 * - Validation và error handling
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Helper Pattern: Tập trung business logic
 * - Strategy Pattern: Xử lý các loại order khác nhau
 * - Factory Pattern: Tạo messages
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingHelper {

    private final OrderHandler orderHandler;
    private final PaymentRequestPublisher paymentRequestPublisher;
    private final RoomRequestPublisher roomOrderRequestPublisher;

    /**
     * Tạo order trực tiếp với thanh toán ngay
     */
    public OrderDTO createDirectOrder(OrderDTO orderDTO) {
        log.info("Creating direct order: {}", orderDTO.id());

        try {
            // 1. Validate order
            validateOrder(orderDTO);

            // 2. Create order entity
            Order order = createOrderEntity(orderDTO);

            // 3. Save order
            Order savedOrder = orderHandler.createOrder(order);

            // 4. Convert back to DTO
            OrderDTO savedOrderDTO = convertToDTO(savedOrder);

            log.info("Direct order created successfully: {}", savedOrderDTO.id());
            return savedOrderDTO;

        } catch (Exception e) {
            log.error("Error creating direct order: {}", orderDTO.id(), e);
            throw new RuntimeException("Failed to create direct order", e);
        }
    }

    /**
     * Tạo order đính kèm room
     */
    public OrderDTO createRoomAttachedOrder(OrderDTO orderDTO, String roomId) {
        log.info("Creating room attached order: {} for room: {}", orderDTO.id(), roomId);

        try {
            // 1. Validate order and room
            validateOrder(orderDTO);
            validateRoomId(roomId);

            // 2. Create order entity
            Order order = createOrderEntity(orderDTO);

            // 3. Save order
            Order savedOrder = orderHandler.createOrder(order);

            // 4. Convert back to DTO
            OrderDTO savedOrderDTO = convertToDTO(savedOrder);

            log.info("Room attached order created successfully: {} for room: {}", savedOrderDTO.id(), roomId);
            return savedOrderDTO;

        } catch (Exception e) {
            log.error("Error creating room attached order: {} for room: {}", orderDTO.id(), roomId, e);
            throw new RuntimeException("Failed to create room attached order", e);
        }
    }

    /**
     * Kích hoạt payment request cho order trực tiếp
     */
    public void triggerDirectPaymentRequest(OrderDTO orderDTO) {
        log.info("Triggering direct payment request for order: {}", orderDTO.id());

        try {
            // 1. Calculate total amount
            BigDecimal totalAmount = calculateTotalAmount(orderDTO);

            // 2. Create payment request message
            PaymentRequestMessage paymentRequestMessage = createPaymentRequestMessage(
                    orderDTO.id(),
                    totalAmount,
                    "DIRECT_PAYMENT"
            );

            // 3. Publish payment request
            paymentRequestPublisher.publish(paymentRequestMessage);

            // 4. Update order status to IN_PROGRESS
            orderHandler.updateOrderStatus(orderDTO.id(), OrderStatus.IN_PROGRESS);

            log.info("Direct payment request triggered successfully for order: {}", orderDTO.id());

        } catch (Exception e) {
            log.error("Error triggering direct payment request for order: {}", orderDTO.id(), e);
            throw new RuntimeException("Failed to trigger direct payment request", e);
        }
    }

    /**
     * Kích hoạt room order request để đính kèm order vào room
     */
    public void triggerRoomOrderRequest(OrderDTO orderDTO, String roomId) {
        log.info("Triggering room order request for order: {} to room: {}", orderDTO.id(), roomId);

        try {
            // 1. Calculate total amount
            BigDecimal totalAmount = calculateTotalAmount(orderDTO);

            // 2. Create room order request message
            RoomRequestMessage roomOrderRequestMessage = createAttachOrderRequest(orderDTO.id(),
                    roomId,
                    orderDTO.customerId()
            );

            // 3. Publish room order request
            roomOrderRequestPublisher.publish(roomOrderRequestMessage);

            // 4. Update order status to NEW (waiting for room attachment)
            orderHandler.updateOrderStatus(orderDTO.id(), OrderStatus.NEW);

            log.info("Room order request triggered successfully for order: {} to room: {}", orderDTO.id(), roomId);

        } catch (Exception e) {
            log.error("Error triggering room order request for order: {} to room: {}", orderDTO.id(), roomId, e);
            throw new RuntimeException("Failed to trigger room order request", e);
        }
    }

    /**
     * Kích hoạt payment request cho order đính kèm room (khi checkout)
     */
    public void triggerRoomOrderPaymentRequest(OrderDTO orderDTO, String roomId) {
        log.info("Triggering room order payment request for order: {} from room: {}", orderDTO.id(), roomId);

        try {
            // 1. Calculate total amount
            BigDecimal totalAmount = calculateTotalAmount(orderDTO);

            // 2. Create payment request message
            PaymentRequestMessage paymentRequestMessage = createPaymentRequestMessage(
                    orderDTO.id(),
                    totalAmount,
                    "ROOM_CHECKOUT_PAYMENT"
            );

            // 3. Publish payment request
            paymentRequestPublisher.publish(paymentRequestMessage);

            // 4. Create detach order request
            RoomRequestMessage detachRequestMessage = createDetachOrderRequest(
                    orderDTO.id(),
                    roomId,
                    orderDTO.customerId()
            );

            // 5. Publish detach request
            roomOrderRequestPublisher.publish(detachRequestMessage);

            log.info("Room order payment request triggered successfully for order: {} from room: {}", orderDTO.id(), roomId);

        } catch (Exception e) {
            log.error("Error triggering room order payment request for order: {} from room: {}", orderDTO.id(), roomId, e);
            throw new RuntimeException("Failed to trigger room order payment request", e);
        }
    }

    /**
     * Validate order
     */
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

    /**
     * Validate room ID
     */
    private void validateRoomId(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be null or empty");
        }
    }

    /**
     * Create order entity from DTO
     */
    private Order createOrderEntity(OrderDTO orderDTO) {
        // TODO: Implement proper mapping from DTO to Entity
        // This is a placeholder implementation
        return new Order(
                orderDTO.id(),
                orderDTO.customerId(),
                orderDTO.tableId(),
                new java.util.ArrayList<>(), // TODO: Map order items
                java.time.LocalDateTime.now()
        );
    }

    /**
     * Convert order entity to DTO
     */
    private OrderDTO convertToDTO(Order order) {
        // TODO: Implement proper mapping from Entity to DTO
        // This is a placeholder implementation
        return new OrderDTO(
                order.getId(),
                order.getCustomerId(),
                order.getTableId(),
                new java.util.ArrayList<>(), // TODO: Map order items
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getCustomerNote(),
                order.getOrderNumber()
        );
    }

    /**
     * Calculate total amount from order items
     */
    private BigDecimal calculateTotalAmount(OrderDTO orderDTO) {
        return orderDTO.items().stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Helper method to create payment request message
     */
    public PaymentRequestMessage createPaymentRequestMessage(String orderId, BigDecimal amount, String paymentMethod) {
        return PaymentRequestMessage.builder()
                .paymentId(UUID.randomUUID().toString())
                .bookingId(orderId)
                .amount(amount)
                .currency("VND")
                .paymentMethod(paymentMethod)
                .customerId("customer-id-placeholder")
                .build();
    }

    /**
     * Helper method to create room request message to attach an order
     */
    public RoomRequestMessage createAttachOrderRequest(String orderId, String roomId, String customerId) {
        return RoomRequestMessage.builder()
                .roomId(roomId)
                .roomType("AttachOrder")
                .customerId(customerId)
                .build();
    }

    /**
     * Helper method to create a room request message to detach an order
     */
    public RoomRequestMessage createDetachOrderRequest(String orderId, String roomId, String customerId) {
        return RoomRequestMessage.builder()
                .roomId(roomId)
                .roomType("DetachOrder")
                .customerId(customerId)
                .build();
    }

}
