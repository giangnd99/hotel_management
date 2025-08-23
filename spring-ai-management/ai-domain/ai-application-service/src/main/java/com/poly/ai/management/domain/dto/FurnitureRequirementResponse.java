package com.poly.ai.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FurnitureRequirementResponse {
    private FurnitureResponse furniture;
    private RoomTypeResponse roomType;
    private Integer requiredQuantity;
}
