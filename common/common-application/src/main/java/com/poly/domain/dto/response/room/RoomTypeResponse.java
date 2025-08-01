package com.poly.domain.dto.response.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeResponse {
    private Integer id;
    private String typeName;
    private String description;
    private String basePrice;
    private int maxOccupancy;
    private List<FurnitureRequirementResponse> furnitureRequirements;
}
