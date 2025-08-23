package com.poly.room.management.domain.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoomTypeRequest {
    private UUID roomTypeId;

    private String typeName;

    private String description;

    @Min(value = 0, message = "New base price must be positive")
    private String basePrice;

    @Min(value = 1, message = "New max occupancy must be at least 1")
    private int maxOccupancy;
    @Nullable
    private List<FurnitureRequirementRequest> furnitureRequirements;
}
