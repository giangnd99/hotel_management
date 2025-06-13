package com.poly.room.management.dao.room.mapper;

import com.poly.room.management.dao.room.entity.RoomTypeFurnitureEntity;
import com.poly.room.management.domain.entity.FurnitureRequirement;
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
        return new FurnitureRequirement(furnitureMapper.toDomainEntity(entity.getFurniture()), entity.getRequireQuantity());
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

    public List<RoomTypeFurnitureEntity> toJpaEntities(List<FurnitureRequirement> domains) {
        if (domains == null) {
            return Collections.emptyList();
        }

        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

}
