package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.InventoryItemId;
import com.poly.room.management.domain.valueobject.FurnitureId;

public class Furniture extends BaseEntity<FurnitureId> {

    private InventoryItemId inventoryItemId;

    public Furniture() {
    }

    public Furniture(InventoryItemId inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public InventoryItemId getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(InventoryItemId inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }
}
