package com.poly.room.management.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomTypeRequest {
    @NotBlank(message = "Type name is required")
    private String typeName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Base price is required")
    @Min(value = 0, message = "Base price must be positive")
    private String basePrice;

    @Min(value = 1, message = "Max occupancy must be at least 1")
    private int maxOccupancy;

    private List<FurnitureRequirementRequest> furnitureRequirements;
}
