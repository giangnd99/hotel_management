package com.poly.room.management.domain.mapper;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.entity.FurnitureRequirement;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class RoomTypeDtoMapper {
    private final FurnitureDtoMapper furnitureMapper;

    public RoomTypeDtoMapper(FurnitureDtoMapper furnitureMapper) {
        this.furnitureMapper = furnitureMapper;
    }

    public RoomType toCreateEntity(CreateRoomTypeRequest request) {
        return RoomType.Builder.builder()
                .typeName(request.getTypeName())
                .description(request.getDescription())
                .basePrice(new Money(new BigDecimal(request.getBasePrice())))
                .maxOccupancy(request.getMaxOccupancy())
                .furnitures(request.getFurnitureRequirements()
                        .stream()
                        .map(r ->
                                new FurnitureRequirement(furnitureMapper.toEntity(r.getFurnitureId()), r.getQuantity())).toList())
                .build();
    }

    public RoomType toUpdateEntity(UpdateRoomTypeRequest request) {
        return RoomType.Builder.builder()
                .id(new RoomTypeId(request.getRoomTypeId()))
                .typeName(request.getTypeName())
                .description(request.getDescription())
                .basePrice(new Money(new BigDecimal(request.getBasePrice())))
                .maxOccupancy(request.getMaxOccupancy())
                .furnitures(request.getFurnitureRequirements().stream()
                        .map(furnitureMapper::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public RoomTypeResponse toResponse(RoomType roomType) {
        return RoomTypeResponse.builder()
                .id(roomType.getId().getValue())
                .typeName(roomType.getTypeName())
                .description(roomType.getDescription())
                .basePrice(roomType.getBasePrice().getAmount().toString())
                .maxOccupancy(roomType.getMaxOccupancy())
                .furnitureRequirements(roomType.getFurnitures().stream()
                        .map(furnitureMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}