package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.FurnitureId;

public class Furniture extends BaseEntity<FurnitureId> {

    private InventoryItemId inventoryItemId;

    public Furniture() {
    }

    public Furniture(InventoryItemId inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    private Furniture(Builder builder) {
        super.setId(builder.id);
        setInventoryItemId(builder.inventoryItemId);
    }

    public void validate() {
        if (inventoryItemId == null
                || inventoryItemId.getValue() == null
                || inventoryItemId.getValue().isEmpty()
                || inventoryItemId.getValue().isBlank()) {
            throw new RoomDomainException("InventoryItemId is null");
        }
    }

    public InventoryItemId getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(InventoryItemId inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
        validate();
    }


    public static final class Builder {
        private FurnitureId id;
        private InventoryItemId inventoryItemId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(FurnitureId val) {
            id = val;
            return this;
        }

        public Builder inventoryItemId(InventoryItemId val) {
            inventoryItemId = val;
            return this;
        }

        public Furniture build() {
            return new Furniture(this);
        }
    }
}
