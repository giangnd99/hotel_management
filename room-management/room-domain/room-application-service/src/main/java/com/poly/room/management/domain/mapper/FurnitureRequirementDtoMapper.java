package com.poly.room.management.domain.mapper;

import com.poly.domain.valueobject.CompositeId;
import com.poly.domain.valueobject.CompositeKey;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.response.FurnitureRequirementResponse;
import com.poly.room.management.domain.entity.FurnitureRequirement;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import org.springframework.stereotype.Component;

@Component
public class FurnitureRequirementMapper {


    public FurnitureRequirement toEntity(FurnitureRequirementRequest request) {
        FurnitureRequirementId id = new FurnitureRequirementId(new CompositeKey<>(
                new FurnitureId(request.getFurnitureId()),
                new RoomTypeId(request.getRoomTypeId())));
        return FurnitureRequirement.Builder.builder()
                .id(id)
                .furniture()
                .roomType()
                .requiredQuantity(request.getQuantity())
                .build();
    }

    public FurnitureRequirementResponse toResponse(FurnitureRequirement furnitureRequirement) {
        return FurnitureRequirementResponse.builder()
                .furnitureId(furnitureRequirement.getFurniture().getId().getValue())
                .furnitureInventoryItemId(furnitureRequirement.getFurniture().getInventoryItemId().getValue())
                .requiredQuantity(furnitureRequirement.getRequiredQuantity())
                .roomTypeId(new RoomTypeId(furnitureRequirement.getId().getValue().getId2().getValue()).getValue())
                .build();
    }

}
