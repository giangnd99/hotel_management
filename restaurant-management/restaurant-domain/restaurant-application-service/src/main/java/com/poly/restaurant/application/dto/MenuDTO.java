package com.poly.restaurant.application.dto;

import java.math.BigDecimal;

public record MenuDTO(
        String id,
        String name,
        String description,
        BigDecimal price,
        String categoryId,
        String status
) {}
