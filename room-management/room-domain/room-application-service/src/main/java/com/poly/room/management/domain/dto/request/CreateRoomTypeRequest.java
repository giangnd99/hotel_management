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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomTypeRequest {
    private String typeName;

    private String description;

    private String basePrice;

    private Integer maxOccupancy;

    @Nullable
    private List<FurnitureRequirementRequest> furnitureRequirements;
}
