package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.response.FurnitureRequirementResponse;
import com.poly.room.management.domain.entity.RoomTypeFurniture;
import com.poly.room.management.domain.port.out.repository.FurnitureRepository;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FurnitureRequirementDtoMapper {

    private final FurnitureRepository furnitureRepository;
    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeFurniture toEntity(FurnitureRequirementRequest request) {

        return RoomTypeFurniture.Builder.builder()
                .furnitureId(new FurnitureId(request.getFurnitureId()))
                .roomTypeId(new RoomTypeId(request.getRoomTypeId()))
                .requiredQuantity(request.getQuantity())
                .build();
    }

    public FurnitureRequirementResponse toResponse(RoomTypeFurniture roomTypeFurniture) {
        return FurnitureRequirementResponse.builder()
                .furnitureId(roomTypeFurniture.getFurniture().getId().getValue())
                .furnitureInventoryItemId(roomTypeFurniture.getFurniture().getInventoryItemId().getValue())
                .requiredQuantity(roomTypeFurniture.getRequiredQuantity())
                .roomTypeId(roomTypeFurniture.getFurniture().getId().getValue())
                .build();
    }
}
