package com.poly.room.management.domain.mapper;

import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.entity.RoomType;
import org.springframework.stereotype.Component;

@Component
public class RoomTypeDtoMapper {

    public RoomTypeResponse toResponse(RoomType roomType) {
        return RoomTypeResponse.builder()
                .id(roomType.getId().getValue())
                .typeName(roomType.getTypeName())
                .description(roomType.getDescription())
                .basePrice(roomType.getBasePrice().getAmount().toString())
                .maxOccupancy(roomType.getMaxOccupancy())
                .build();
    }
}