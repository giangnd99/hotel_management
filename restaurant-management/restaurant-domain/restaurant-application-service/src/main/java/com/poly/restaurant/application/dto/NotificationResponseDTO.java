package com.poly.restaurant.application.dto;

import java.time.LocalDateTime;

public record NotificationResponseDTO(
    String notificationId,
    String customerId,
    String type,
    String status,
    LocalDateTime sentAt,
    String message
) {}
