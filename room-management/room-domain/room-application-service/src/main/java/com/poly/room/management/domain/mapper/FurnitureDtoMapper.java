package com.poly.room.management.domain.mapper;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.entity.Furniture;
import org.springframework.stereotype.Component;

@Component
public class FurnitureDtoMapper {

    public Furniture toEntity(FurnitureCreationRequest request) {
        return Furniture.Builder.builder()
                .inventoryItemId(new InventoryItemId(request.getInventoryItemId()))
                .build();
    }

    public FurnitureResponse toResponse(Furniture furniture) {
        return FurnitureResponse.builder()
                .id(furniture.getId().getValue())
                .inventoryItemId(furniture.getInventoryItemId().getValue())
                .build();
    }

}