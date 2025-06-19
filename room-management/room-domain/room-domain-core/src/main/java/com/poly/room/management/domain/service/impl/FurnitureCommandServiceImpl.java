package com.poly.room.management.domain.service.impl;

public class FurnitureCommandServiceImpl implements FurnitureCommandService {
    
    @Override
    public Furniture createFurniture(InventoryItemId inventoryItemId) {
        if (inventoryItemId == null) {
            throw new RoomDomainException("InventoryItemId cannot be null");
        }
        
        Furniture newFurniture = new Furniture(inventoryItemId);
        newFurniture.validate();
        return newFurniture;
    }

    @Override
    public Furniture updateFurnitureInventoryItem(Furniture furniture, InventoryItemId newInventoryItemId) {
        if (newInventoryItemId == null) {
            throw new RoomDomainException("New InventoryItemId cannot be null");
        }
        
        furniture.setInventoryItemId(newInventoryItemId);
        furniture.validate();
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