package com.poly.room.management.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureRequirementRequest {
    @NotNull(message = "Furniture ID is required")
    private Integer furnitureId;

    @NotNull(message = "Room type ID is required")
    private Integer roomTypeId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
