package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomId;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureId;
import com.poly.room.management.domain.valueobject.FurnitureRequirementId;
import com.poly.room.management.domain.valueobject.RoomTypeId;

public class FurnitureRequirement extends BaseEntity<FurnitureRequirementId> {

    private Furniture furniture;
    private FurnitureId furnitureId;
    private RoomType roomType;
    private int requiredQuantity;
    private RoomTypeId roomTypeId;
    public static final int DEFAULT_QUANTITY = 1;

    public FurnitureRequirement(Furniture furniture, int requirementQuantity, RoomTypeId roomTypeId) {
        this.furniture = furniture;
        this.requiredQuantity = requirementQuantity;
        this.roomTypeId = roomTypeId;
    }

    private FurnitureRequirement(Builder builder) {
        super.setId(builder.id);
        setFurniture(builder.furniture);
        furnitureId = builder.furnitureId;
        roomType = builder.roomType;
        setRequiredQuantity(builder.requiredQuantity);
        setRoomTypeId(builder.roomTypeId);
    }

    public void validate() {
        if (furniture == null || furniture.getId() == null) {
            throw new RoomDomainException("Furniture for requirement cannot be null or have null ID.");
        }
        if (requiredQuantity <= 0) {
            throw new RoomDomainException("Required quantity for furniture must be positive.");
        }
        if (roomTypeId == null) {
            throw new RoomDomainException("RoomType cannot be null for a FurnitureRequirement.");
        }
        if (furniture.getInventoryItemId() == null) {
            throw new RoomDomainException("InventoryItemId for furniture cannot be null.");
        }
        if (furniture.getInventoryItemId().getValue() == null) {
            throw new RoomDomainException("InventoryItemId for furniture cannot be null.");
        }
        if (furniture.getInventoryItemId().getValue().isEmpty() || furniture.getInventoryItemId().getValue().isBlank()) {
            throw new RoomDomainException("InventoryItemId for furniture cannot be empty or blank.");
        }
    }

    public void setFurniture(Furniture furniture) {
        this.furniture = furniture;
    }

    public void setRoomTypeId(RoomTypeId roomTypeId) {
        this.roomTypeId = roomTypeId;
        validate();
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

    public static final class Builder {
        private Furniture furniture;
        private FurnitureId furnitureId;
        private RoomType roomType;
        private int requiredQuantity;
        private RoomTypeId roomTypeId;
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

        public Builder furnitureId(FurnitureId val) {
            furnitureId = val;
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

        public Builder roomTypeId(RoomTypeId val) {
            roomTypeId = val;
            return this;
        }

        public Builder id(FurnitureRequirementId val) {
            id = val;
            return this;
        }

        public FurnitureRequirement build() {
            return new FurnitureRequirement(this);
        }
    }
}
