package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.service.sub.FurnitureCommandService;
import com.poly.room.management.domain.valueobject.FurnitureId;

import java.util.UUID;

public class FurnitureCommandServiceImpl implements FurnitureCommandService {

    @Override
    public Furniture createFurniture(InventoryItemId inventoryItemId) {
        if (inventoryItemId == null) {
            throw new RoomDomainException("InventoryItemId cannot be null");
        }

        Furniture newFurniture = Furniture.Builder.builder()
                .id(new FurnitureId(UUID.randomUUID().variant()))
                .name(inventoryItemId.getValue())
                .build();
        return newFurniture;
    }

    @Override
    public Furniture updateFurnitureInventoryItem(Furniture furniture, InventoryItemId newInventoryItemId) {
        if (newInventoryItemId == null) {
            throw new RoomDomainException("New InventoryItemId cannot be null");
        }


        return furniture;
    }

    @Override
    public void deleteFurniture(Furniture furniture) {
        // Validation for usage happens at application service level
        if (furniture == null) {
            throw new RoomDomainException("Cannot delete null furniture");
        }
    }
}