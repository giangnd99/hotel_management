package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.Money;
import com.poly.room.management.dao.room.entity.RoomTypeEntity;
import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import com.poly.room.management.dao.room.repository.RoomTypeFurnitureJpaRepository;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomTypeMapper {

    private final RoomTypeFurnitureJpaRepository repository;
    private final FurnitureMapper furnitureMapper;
    private final RoomTypeFurnitureMapper roomTypeFurnitureMapper;

    public RoomType toDomainEntity(RoomTypeEntity entity) {
        List<RoomTypeFurnitureEntity> furnitureEntities = repository.findAllByRoomTypeId(
                entity.getRoomTypeId());

        return RoomType.Builder.builder()
                .id(new RoomTypeId(entity.getRoomTypeId()))
                .typeName(entity.getTypeName())
                .basePrice(new Money(entity.getBasePrice()))
                .description(entity.getDescription())
                .maxOccupancy(entity.getMaxOccupancy())
                .furnitures(roomTypeFurnitureMapper.toDomainEntities(furnitureEntities))
                .build();
    }

    public RoomTypeEntity toEntity(RoomType domain) {
        return RoomTypeEntity.builder()
                .roomTypeId(domain.getId().getValue())
                .typeName(domain.getTypeName())
                .description(domain.getDescription())
                .basePrice(domain.getBasePrice().getAmount())
                .maxOccupancy(domain.getMaxOccupancy())
                .build();
    }
}
