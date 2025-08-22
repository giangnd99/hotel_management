package com.poly.room.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class RoomTypeDto {
    private UUID typeId;
    private String typeName;
    private String description;
    private BigDecimal basePrice;
    private Integer maxOccupancy;
    private String amenities;
    private Boolean isActive;
}