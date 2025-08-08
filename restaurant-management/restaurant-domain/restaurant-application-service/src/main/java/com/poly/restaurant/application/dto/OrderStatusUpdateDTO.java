package com.poly.restaurant.application.dto;

public record OrderStatusUpdateDTO(
    String orderId,
    String paymentStatus,
    String paymentId,
    String message
) {}
