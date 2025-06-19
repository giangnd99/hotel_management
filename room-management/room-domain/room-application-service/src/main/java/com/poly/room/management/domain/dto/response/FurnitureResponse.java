package com.poly.room.management.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FurnitureResponse {
    private Integer id;
    private String inventoryItemId;
}
