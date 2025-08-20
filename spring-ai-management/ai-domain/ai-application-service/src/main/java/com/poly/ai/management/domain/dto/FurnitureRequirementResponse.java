package com.poly.ai.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FurnitureRequirementResponse {
    private String furnitureName;
    private String description;
    private String material;
    private String brand;
    private String warrantyInfo;
    private String maintenanceSchedule;
}
