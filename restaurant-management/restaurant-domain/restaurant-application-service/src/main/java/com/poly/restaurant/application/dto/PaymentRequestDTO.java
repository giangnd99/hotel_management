package com.poly.restaurant.application.dto;

import java.math.BigDecimal;

public record PaymentRequestDTO(
    String orderId,
    String customerId,
    BigDecimal amount,
    String paymentMethod,
    String description
) {}
