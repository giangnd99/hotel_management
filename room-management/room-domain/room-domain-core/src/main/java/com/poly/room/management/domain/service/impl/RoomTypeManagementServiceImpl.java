package com.poly.room.management.domain.service.impl;

public class RoomTypeCommandServiceImpl implements RoomTypeCommandService {
    
    @Override
    public RoomType createRoomType(String typeName, String description, String basePriceStr, 
            int maxOccupancy, List<FurnitureRequirement> furnitureItems) {
            
        BigDecimal basePriceAmount;
        try {
            basePriceAmount = new BigDecimal(basePriceStr);
            if (basePriceAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RoomDomainException("Base price must be positive");
            }
        } catch (NumberFormatException e) {
            throw new RoomDomainException("Invalid base price format: " + basePriceStr);
        }

        validateFurnitureRequirements(furnitureItems);

        RoomType newRoomType = new RoomType(typeName, description, 
                new Money(basePriceAmount), maxOccupancy, new ArrayList<>(furnitureItems));
        
        newRoomType.validateRoomType();
        return newRoomType;
    }

    @Override
    public RoomType updateRoomTypeDetails(RoomType roomType, String newTypeName, 
            String newDescription, String newBasePriceStr, int newMaxOccupancy) {
            
        roomType.setTypeName(newTypeName);
        roomType.setDescription(newDescription);

        BigDecimal newBasePriceAmount;
        try {
            newBasePriceAmount = new BigDecimal(newBasePriceStr);
            if (newBasePriceAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RoomDomainException("New base price must be positive");
            }
        } catch (NumberFormatException e) {
            throw new RoomDomainException("Invalid new base price format: " + newBasePriceStr);
        }

        roomType.setBasePrice(new Money(newBasePriceAmount));
        roomType.setMaxOccupancy(newMaxOccupancy);
        roomType.validateRoomType();
        
        return roomType;
    }

    @Override
    public void deleteRoomType(RoomType roomType) {
        // Validation happens at application service level
    }

    @Override
    public RoomType addFurnitureToRoomType(RoomType roomType, Furniture furniture, int quantity) {
        validateFurniture(furniture);
        if (quantity <= 0) {
            throw new RoomDomainException("Furniture quantity must be positive");
        }

        FurnitureRequirementId requirementId = new FurnitureRequirementId(
                new CompositeKey<>(furniture.getId(), roomType.getId()));
                
        FurnitureRequirement requirement = FurnitureRequirement.Builder.builder()
                .id(requirementId)
                .furniture(furniture)
                .roomType(roomType)
                .requiredQuantity(quantity)
                .build();

        roomType.addFurnitureRequirement(requirement);
        roomType.validateRoomType();
        
        return roomType;
    }

    @Override
    public RoomType removeFurnitureFromRoomType(RoomType roomType, FurnitureId furnitureId) {
        roomType.removeFurnitureRequirement(furnitureId);
        roomType.validateRoomType();
        return roomType;
    }

    @Override
    public RoomType updateFurnitureQuantityInRoomType(RoomType roomType, 
            FurnitureId furnitureId, int newQuantity) {
        if (newQuantity <= 0) {
            throw new RoomDomainException("New furniture quantity must be positive");
        }
        
        roomType.updateFurnitureRequirementQuantity(furnitureId, newQuantity);
        roomType.validateRoomType();
        return roomType;
    }

    private void validateFurnitureRequirements(List<FurnitureRequirement> requirements) {
        if (requirements == null) {
            return;
        }
        
        for (FurnitureRequirement requirement : requirements) {
            validateFurniture(requirement.getFurniture());
            if (requirement.getRequiredQuantity() <= 0) {
                throw new RoomDomainException("Required quantity must be positive");
            }
        }
    }

    private void validateFurniture(Furniture furniture) {
        if (furniture == null || furniture.getId() == null) {
            throw new RoomDomainException("Furniture cannot be null or have null ID");
        }
    }
}