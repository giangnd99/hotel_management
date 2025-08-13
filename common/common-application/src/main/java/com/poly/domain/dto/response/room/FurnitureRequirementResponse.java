package com.poly.domain.dto.response.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureRequirementResponse {
    private Integer furnitureId;
    private String furnitureInventoryItemId; // Hiển thị ID của vật phẩm tồn kho
    private Integer roomTypeId;
    private int requiredQuantity;
}
