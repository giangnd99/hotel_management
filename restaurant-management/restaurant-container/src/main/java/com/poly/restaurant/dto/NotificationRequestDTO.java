package com.poly.restaurant.dto;

public record NotificationRequestDTO(
    String customerId,
    String type,
    String title,
    String message,
    String orderId
) {}
