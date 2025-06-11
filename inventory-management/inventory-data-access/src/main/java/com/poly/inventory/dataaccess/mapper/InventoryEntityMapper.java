package com.poly.inventory.dataaccess.mapper;

import com.poly.inventory.dataaccess.entity.InventoryEntity;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.ItemId;
import com.poly.inventory.domain.value_object.Quantity;

public class InventoryEntityMapper {

    private InventoryEntityMapper() {
    }

    public static InventoryItem toDomain(InventoryEntity entity) {
        if (entity == null) return null;

        return new InventoryItem(
                ItemId.of(entity.getItemId().getValue()),
                entity.getItemName(),
                entity.getCategory(),
                new Quantity(entity.getQuantity()),
                entity.getUnitPrice(),
                entity.getMinimumQuantity()
        );
    }

    public static InventoryEntity toEntity(InventoryItem item) {
        if (item == null) return null;

        InventoryEntity entity = new InventoryEntity();
        entity.setItemId(item.getItemId());
        entity.setItemName(item.getItemName());
        entity.setCategory(item.getCategory());
        entity.setQuantity(item.getQuantity().getValue());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setMinimumQuantity(item.getMinimumQuantity());
        return entity;
    }
}
