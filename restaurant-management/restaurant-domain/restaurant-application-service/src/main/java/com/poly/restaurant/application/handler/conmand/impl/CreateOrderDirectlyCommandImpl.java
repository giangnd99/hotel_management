package com.poly.restaurant.application.handler.conmand.impl;

import com.poly.restaurant.application.dto.OrderDTO;
import com.poly.restaurant.application.handler.conmand.CreateOrderDirectlyCommand;
import com.poly.restaurant.application.handler.conmand.impl.helper.OrderProcessingHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Implementation cho CreateOrderDirectlyCommand
 * 
 * NGHIỆP VỤ:
 * - Tạo order với thanh toán trực tiếp
 * - Kích hoạt payment request ngay sau khi tạo order
 * - Quản lý quy trình order trực tiếp
 * 
 * PATTERNS ÁP DỤNG:
 * - Command Pattern: Thực hiện business command
 * - Strategy Pattern: Xử lý order trực tiếp
 * - Chain of Responsibility: Quy trình tạo order -> payment
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CreateOrderDirectlyCommandImpl implements CreateOrderDirectlyCommand {

    private final OrderProcessingHelper orderProcessingHelper;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        log.info("Creating direct order with immediate payment: {}", orderDTO.id());
        
        try {
            // 1. Tạo order
            OrderDTO createdOrder = orderProcessingHelper.createDirectOrder(orderDTO);
            
            // 2. Kích hoạt payment request ngay lập tức
            orderProcessingHelper.triggerDirectPaymentRequest(createdOrder);
            
            log.info("Direct order created and payment request triggered successfully: {}", createdOrder.id());
            return createdOrder;
            
        } catch (Exception e) {
            log.error("Error creating direct order: {}", orderDTO.id(), e);
            throw new RuntimeException("Failed to create direct order", e);
        }
    }

    @Override
    public void triggerPaymentRequest(OrderDTO orderDTO) {
        log.info("Triggering payment request for direct order: {}", orderDTO.id());
        
        try {
            orderProcessingHelper.triggerDirectPaymentRequest(orderDTO);
            log.info("Payment request triggered successfully for direct order: {}", orderDTO.id());
            
        } catch (Exception e) {
            log.error("Error triggering payment request for direct order: {}", orderDTO.id(), e);
            throw new RuntimeException("Failed to trigger payment request for direct order", e);
        }
    }
}
