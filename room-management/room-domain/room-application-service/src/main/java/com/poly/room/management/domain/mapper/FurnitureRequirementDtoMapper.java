package com.poly.room.management.domain.mapper;

import com.poly.domain.valueobject.CompositeKey;
import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.response.FurnitureRequirementResponse;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.FurnitureRequirement;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FurnitureRequirementDtoMapper {

    private final FurnitureRepository furnitureRepository;
    private final RoomTypeRepository roomTypeRepository;

    public FurnitureRequirement toEntity(FurnitureRequirementRequest request) {

        return FurnitureRequirement.Builder.builder()
                .furnitureId(new FurnitureId(request.getFurnitureId()))
                .roomTypeId(new RoomTypeId(request.getRoomTypeId()))
                .requiredQuantity(request.getQuantity())
                .build();
    }

    public FurnitureRequirementResponse toResponse(FurnitureRequirement furnitureRequirement) {
        return FurnitureRequirementResponse.builder()
                .furnitureId(furnitureRequirement.getFurniture().getId().getValue())
                .furnitureInventoryItemId(furnitureRequirement.getFurniture().getInventoryItemId().getValue())
                .requiredQuantity(furnitureRequirement.getRequiredQuantity())
                .roomTypeId(furnitureRequirement.getFurniture().getId().getValue())
                .build();
    }
}
