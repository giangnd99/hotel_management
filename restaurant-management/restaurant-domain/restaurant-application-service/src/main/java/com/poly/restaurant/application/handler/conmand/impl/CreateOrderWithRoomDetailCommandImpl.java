package com.poly.restaurant.application.handler.conmand.impl;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.handler.conmand.CreateOrderWithRoomDetailCommand;
import com.poly.restaurant.application.handler.helper.OrderProcessingHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Implementation cho CreateOrderWithRoomDetailCommand
 * <p>
 * NGHIỆP VỤ:
 * - Tạo order đính kèm vào room
 * - Kích hoạt room order request để đính kèm order
 * - Quản lý quy trình order đính kèm room
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Command Pattern: Thực hiện business command
 * - Strategy Pattern: Xử lý order đính kèm room
 * - Chain of Responsibility: Quy trình tạo order -> room attachment
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateOrderWithRoomDetailCommandImpl implements CreateOrderWithRoomDetailCommand {

    private final OrderProcessingHelper orderProcessingHelper;

    @Override
    public OrderDTO createOrderWithRoomDetail(OrderDTO orderDTO) {
        log.info("Creating room attached order: {}", orderDTO.id());

        try {
            // TODO: Extract roomId from orderDTO or add as parameter
            // For now, we'll use a placeholder room ID
            String roomId = extractRoomIdFromOrder(orderDTO);

            // 1. Tạo order đính kèm room
            OrderDTO createdOrder = orderProcessingHelper.createRoomAttachedOrder(orderDTO, roomId);

            // 2. Kích hoạt room order request để đính kèm order vào room
            orderProcessingHelper.triggerRoomOrderRequest(createdOrder, roomId);

            log.info("Room attached order created and room order request triggered successfully: {} for room: {}",
                    createdOrder.id(), roomId);
            return createdOrder;

        } catch (Exception e) {
            log.error("Error creating room attached order: {}", orderDTO.id(), e);
            throw new RuntimeException("Failed to create room attached order", e);
        }
    }

    @Override
    public void triggerPaymentRequest(OrderDTO orderDTO) {
        log.info("Triggering payment request for room attached order: {}", orderDTO.id());

        try {
            // TODO: Extract roomId from orderDTO or add as parameter
            String roomId = extractRoomIdFromOrder(orderDTO);

            // Kích hoạt payment request cho order đính kèm room (khi checkout)
            orderProcessingHelper.triggerRoomOrderPaymentRequest(orderDTO, roomId);

            log.info("Payment request triggered successfully for room attached order: {} from room: {}",
                    orderDTO.id(), roomId);

        } catch (Exception e) {
            log.error("Error triggering payment request for room attached order: {}", orderDTO.id(), e);
            throw new RuntimeException("Failed to trigger payment request for room attached order", e);
        }
    }

    /**
     * Extract room ID from order DTO
     * TODO: Implement proper room ID extraction logic
     */
    private String extractRoomIdFromOrder(OrderDTO orderDTO) {
        // TODO: This is a placeholder implementation
        // In a real implementation, you might:
        // 1. Extract room ID from orderDTO.customerNote() if it contains room info
        // 2. Add roomId field to OrderDTO
        // 3. Query customer service to get room information
        // 4. Use a separate parameter for room ID

        // For now, we'll use a default room ID
        // This should be replaced with actual room ID extraction logic
        return "ROOM_001"; // Placeholder room ID
    }
}
