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
public class UpdateRoomTypeRequest {
    @NotNull(message = "Room type ID is required for update")
    private Integer roomTypeId;

    @NotBlank(message = "New type name is required")
    private String typeName;

    @NotBlank(message = "New description is required")
    private String description;

    @NotNull(message = "New base price is required")
    @Min(value = 0, message = "New base price must be positive")
    private String basePrice;

    @Min(value = 1, message = "New max occupancy must be at least 1")
    private int maxOccupancy;

    @NotNull(message = "New furniture requirements are required")
    private List<FurnitureRequirementRequest> furnitureRequirements;
}
