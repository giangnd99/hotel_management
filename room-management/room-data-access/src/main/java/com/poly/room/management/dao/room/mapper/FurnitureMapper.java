package com.poly.room.management.dao.room.mapper;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.dao.room.entity.FurnitureEntity;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.valueobject.FurnitureId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FurnitureMapper {

    public Furniture toDomainEntity(FurnitureEntity entity) {
        return Furniture.Builder.builder()
                .id(new FurnitureId(entity.getFurnitureId()))
                .inventoryItemId(new InventoryItemId(entity.getInventoryItemId()))
                .build();
    }

    public FurnitureEntity toEntity(Furniture domain) {
        return FurnitureEntity.builder()
                .furnitureId(domain.getId().getValue())
                .inventoryItemId(domain.getInventoryItemId().getValue())
                .build();
    }
}
