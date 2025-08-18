package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
import com.poly.room.management.domain.dto.response.FurnitureRequirementResponse;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.RoomType;
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
                .furniture(Furniture.Builder.builder()
                        .id(new FurnitureId(request.getFurnitureId()))
                        .build())
                .roomType(RoomType.Builder.builder()
                        .id(new RoomTypeId(request.getRoomTypeId()))
                        .build())
                .requiredQuantity(request.getQuantity())
                .build();
    }

    public FurnitureRequirementResponse toResponse(RoomTypeFurniture roomTypeFurniture) {
        return FurnitureRequirementResponse.builder()
                .furniture(roomTypeFurniture.getFurniture())
                .requiredQuantity(roomTypeFurniture.getRequiredQuantity())
                .roomType(roomTypeFurniture.getRoomType())
                .build();
    }
}
