package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.service.sub.FurnitureQueryService;
import com.poly.room.management.domain.valueobject.FurnitureId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FurnitureQueryServiceImpl implements FurnitureQueryService {
    
    @Override
    public Optional<Furniture> getFurnitureById(List<Furniture> allFurnitures, FurnitureId furnitureId) {
        if (furnitureId == null) {
            throw new RoomDomainException("FurnitureId cannot be null");
        }
        
        return allFurnitures.stream()
                .filter(f -> f.getId().equals(furnitureId))
                .findFirst();
    }


    @Override
    public List<Furniture> getAllFurnitures(List<Furniture> allFurnitures) {
        return new ArrayList<>(allFurnitures); // Return a copy to prevent modification
    }
}