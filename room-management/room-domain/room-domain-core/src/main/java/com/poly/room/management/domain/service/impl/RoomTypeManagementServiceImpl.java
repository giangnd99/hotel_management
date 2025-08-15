package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.CompositeKey;
import com.poly.domain.valueobject.Money;
import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.entity.FurnitureRequirement;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.service.sub.RoomTypeCommandService;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import com.poly.room.management.domain.valueobject.RoomTypeId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomTypeManagementServiceImpl implements RoomTypeCommandService {

    @Override
    public RoomType createRoomType(String typeName, String description, String basePriceStr,
                                   int maxOccupancy, List<FurnitureRequirement> furnitureItems) {

        Money basePriceAmount;
        try {
            basePriceAmount = new Money(new BigDecimal(basePriceStr));
            if (!basePriceAmount.isGreaterThanZero()) {
                throw new RoomDomainException("Base price must be positive");
            }
        } catch (NumberFormatException e) {
            throw new RoomDomainException("Invalid base price format: " + basePriceStr);
        }

        validateFurnitureRequirements(furnitureItems);

        RoomType newRoomType = RoomType.Builder.builder()
                .typeName(typeName)
                .description(description)
                .basePrice(basePriceAmount)
                .maxOccupancy(maxOccupancy)
                .furnitures(new ArrayList<>(furnitureItems))
                .build();

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

        FurnitureRequirementId requirementId = new FurnitureRequirementId(UUID.randomUUID().variant());

        FurnitureRequirement requirement = FurnitureRequirement.Builder.builder()
                .id(requirementId)
                .furniture(furniture)
                .roomTypeId(roomType.getId())
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