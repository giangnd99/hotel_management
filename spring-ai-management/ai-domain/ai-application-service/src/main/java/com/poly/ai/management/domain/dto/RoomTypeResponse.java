package com.poly.ai.management.domain.dto;

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
public class RoomTypeResponse {
    private Long typeId;
    private String typeName;
    private String description;
    private BigDecimal basePrice;
    private Integer maxOccupancy;
    private String amenities;
    private Boolean isActive;
}
