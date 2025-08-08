package com.poly.restaurant.dto;

public record OrderStatusUpdateDTO(
    String orderId,
    String paymentStatus,
    String paymentId,
    String message
) {}
