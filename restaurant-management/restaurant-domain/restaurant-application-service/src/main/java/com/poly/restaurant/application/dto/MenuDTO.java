package com.poly.restaurant.application.dto;

import java.math.BigDecimal;

public record MenuDTO(
        Integer id,
        String name,
        String description,
        BigDecimal price,
        String category,
        Integer quantity,
        String status
) {}
