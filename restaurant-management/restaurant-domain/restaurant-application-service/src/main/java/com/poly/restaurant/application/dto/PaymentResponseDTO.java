package com.poly.restaurant.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponseDTO(
    String paymentId,
    String orderId,
    String customerId,
    BigDecimal amount,
    String status,
    String paymentMethod,
    LocalDateTime processedAt,
    String message
) {}
