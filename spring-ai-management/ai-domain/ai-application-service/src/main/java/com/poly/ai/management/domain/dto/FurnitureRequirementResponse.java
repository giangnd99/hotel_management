package com.poly.ai.management.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureRequirementResponse {
    private String furnitureName;
    private String description;
    private String material;
    private String brand;
    private String warrantyInfo;
    private String maintenanceSchedule;
}
