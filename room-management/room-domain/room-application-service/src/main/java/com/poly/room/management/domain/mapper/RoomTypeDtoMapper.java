package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.response.FurnitureRequirementResponse;
import com.poly.room.management.domain.dto.response.FurnitureResponse;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.entity.RoomType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoomTypeDtoMapper {

    public RoomTypeResponse toResponse(RoomType roomType) {
        return RoomTypeResponse.builder()
                .id(roomType.getId().getValue())
                .typeName(roomType.getTypeName())
                .description(roomType.getDescription())
                .basePrice(roomType.getBasePrice().getAmount().toString())
                .maxOccupancy(roomType.getMaxOccupancy())
                .furnitureRequirements(roomType.getFurnituresRequirements().stream().map(
                        roomTypeFurniture -> FurnitureRequirementResponse.builder()
                                .requiredQuantity(roomTypeFurniture.getRequiredQuantity())
                                .furniture(FurnitureResponse.builder().id(UUID.fromString(roomTypeFurniture.getFurniture().getId().getValue().toString()))
                                        .name(roomTypeFurniture.getFurniture().getName())
                                        .price(roomTypeFurniture.getFurniture().getPrice().getAmount().toString())
                                        .build())
                                .build()
                ).toList())
                .build();
    }
}