package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import com.poly.room.management.domain.valueobject.RoomTypeId;


public class RoomTypeFurniture extends BaseEntity<FurnitureRequirementId> {

    private Furniture furniture;
    private RoomType roomType;
    private int requiredQuantity;
    public static final int DEFAULT_QUANTITY = 1;

    public RoomTypeFurniture(Furniture furnitureId, int requirementQuantity, RoomTypeId roomTypeId) {
        this.furniture = furnitureId;
        this.requiredQuantity = requirementQuantity;
    }

    private RoomTypeFurniture(Builder builder) {
        super.setId(builder.id);
        setFurniture(builder.furniture);
        roomType = builder.roomType;
        setRequiredQuantity(builder.requiredQuantity);
    }

    public void validate() {
        if (furniture == null) {
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

    public RoomType getRoomType() {
        return roomType;
    }

    public Furniture getFurniture() {
        return furniture;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public static final class Builder {
        private RoomType roomType;
        private int requiredQuantity;
        private Furniture furniture;
        private FurnitureRequirementId id;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder furniture(Furniture val) {
            furniture = val;
            return this;
        }

        public Builder roomType(RoomType val) {
            roomType = val;
            return this;
        }

        public Builder requiredQuantity(int val) {
            requiredQuantity = val;
            return this;
        }


        public Builder id(FurnitureRequirementId val) {
            id = val;
            return this;
        }

        public RoomTypeFurniture build() {
            return new RoomTypeFurniture(this);
        }
    }
}
