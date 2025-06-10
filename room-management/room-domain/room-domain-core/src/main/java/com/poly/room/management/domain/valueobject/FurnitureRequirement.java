package com.poly.room.management.domain.valueobject;

import com.poly.room.management.domain.entity.Furniture;
import com.poly.room.management.domain.exception.RoomDomainException;

public class FurnitureRequirement {

    private Furniture furniture;
    private int requiredQuantity;
    public static final int DEFAULT_QUANTITY = 1;

    public FurnitureRequirement(Furniture furniture, int requirementQuantity) {
        validate();
        this.furniture = furniture;
        this.requiredQuantity = requirementQuantity;
    }

    public void validate() {
        if (furniture == null || furniture.getId() == null) {
            throw new RoomDomainException("Furniture for requirement cannot be null or have null ID.");
        }
        if (requiredQuantity <= 0) {
            throw new RoomDomainException("Required quantity for furniture must be positive.");
        }
    }

    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        if (requiredQuantity <= 0) {
            throw new RoomDomainException("Required quantity must be positive.");
        }
        this.requiredQuantity = requiredQuantity;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }
}
