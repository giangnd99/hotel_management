package com.poly.room.management.domain.mapper;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.dto.request.FurnitureCreationRequest;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.response.FurnitureRequirementResponse;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.entity.FurnitureRequirement;
import com.poly.room.management.domain.valueobject.RoomTypeId;
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

    public FurnitureRequirementResponse toRoomFurnitureRequirementResponse(FurnitureRequirement furnitureRequirement) {
        return FurnitureRequirementResponse.builder()
                .furnitureId(furnitureRequirement.getFurniture().getId().getValue())
                .furnitureInventoryItemId(furnitureRequirement.getFurniture().getInventoryItemId().getValue())
                .requiredQuantity(furnitureRequirement.getRequiredQuantity())
                .roomTypeId(new RoomTypeId(furnitureRequirement.getId().getValue().getId2().getValue()).getValue())
                .build();
    }
}