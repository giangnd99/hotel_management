package com.poly.room.management.domain.service.impl;

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
    public Optional<Furniture> getFurnitureByInventoryItemId(List<Furniture> allFurnitures, 
            InventoryItemId inventoryItemId) {
        if (inventoryItemId == null) {
            throw new RoomDomainException("InventoryItemId cannot be null");
        }
        
        return allFurnitures.stream()
                .filter(f -> f.getInventoryItemId().equals(inventoryItemId))
                .findFirst();
    }

    @Override
    public List<Furniture> getAllFurnitures(List<Furniture> allFurnitures) {
        return new ArrayList<>(allFurnitures); // Return a copy to prevent modification
    }
}