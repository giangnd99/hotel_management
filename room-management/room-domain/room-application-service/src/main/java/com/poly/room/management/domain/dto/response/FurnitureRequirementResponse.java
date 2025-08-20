package com.poly.room.management.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.RoomType;
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
    private Furniture furniture;
    private RoomType roomType;
    private int requiredQuantity;
}
