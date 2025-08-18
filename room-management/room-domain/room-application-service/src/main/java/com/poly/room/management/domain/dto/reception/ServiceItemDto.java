package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItemDto {
    private String serviceId;
    private String serviceName;
    private String description;
    private String category;
    private BigDecimal price;
    private String currency;
    private Boolean isAvailable;
    private Integer preparationTime; // in minutes
    private String imageUrl;
    private String allergens;
    private String dietaryInfo; // VEGETARIAN, VEGAN, GLUTEN_FREE, etc.
    private Boolean isPopular;
    private Double rating;
    private Integer reviewCount;
}