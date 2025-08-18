package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryDto {
    private String categoryName;
    private String description;
    private List<ServiceItemDto> services;
    private Boolean isAvailable;
    private String operatingHours;
    private BigDecimal averagePrice;
    private Integer totalServices;
}
