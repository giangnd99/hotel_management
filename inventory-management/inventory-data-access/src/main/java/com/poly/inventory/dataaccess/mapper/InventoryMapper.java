package com.poly.inventory.dataaccess.mapper;

import com.poly.inventory.dataaccess.entity.InventoryEntity;
import com.poly.inventory.domain.model.entity.InventoryItem;
import com.poly.inventory.domain.model.value_object.Quantity;

//public class InventoryMapper {
//
//    public static InventoryItem toDomain(InventoryEntity entity) {
//        return new InventoryItem(
//                entity.getItemId(),
//                entity.getItemName(),
//                entity.getCategory(),
//                new Quantity(entity.getQuantity())
//        );
//    }
//
//    public static InventoryEntity toEntity(InventoryItem item) {
//        InventoryEntity entity = new InventoryEntity();
//        entity.setItemId(item.getItemId());
//        entity.setItemName(item.getItemName());
//        entity.setCategory(item.getCategory());
//        entity.setQuantity(item.getQuantity().intValue());
//        return entity;
//    }
//}
