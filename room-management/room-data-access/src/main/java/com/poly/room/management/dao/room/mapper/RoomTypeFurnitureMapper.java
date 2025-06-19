package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.CompositeKey;
import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import com.poly.room.management.domain.entity.FurnitureRequirement;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import com.poly.room.management.domain.valueobject.RoomTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomTypeFurnitureMapper {

    private final FurnitureMapper furnitureMapper;

    public FurnitureRequirement toDomain(RoomTypeFurnitureEntity entity) {
        return FurnitureRequirement.Builder.builder()
                .id(new FurnitureRequirementId(
                        new CompositeKey<>(
                                new FurnitureId(entity.getFurniture().getFurnitureId()),
                                new RoomTypeId(entity.getRoomType().getRoomTypeId()))))
                .roomTypeId(new RoomTypeId(entity.getRoomType().getRoomTypeId()))
                .furniture(furnitureMapper.toDomainEntity(entity.getFurniture()))
                .requiredQuantity(entity.getRequireQuantity())
                .build();
    }

    public RoomTypeFurnitureEntity toEntity(FurnitureRequirement domain) {
        RoomTypeFurnitureEntity entity = new RoomTypeFurnitureEntity();
        entity.setFurniture(furnitureMapper.toEntity(domain.getFurniture()));
        entity.setRequireQuantity(domain.getRequiredQuantity());
        return entity;

    }

    public List<FurnitureRequirement> toDomainEntities(List<RoomTypeFurnitureEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}
