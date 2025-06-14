package com.poly.inventory.application.mapper;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.ItemId;
import com.poly.inventory.domain.value_object.Quantity;

public class InventoryDtoMapper {

    public static InventoryItemDto toDto(InventoryItem item) {
        InventoryItemDto dto = new InventoryItemDto();
        dto.itemId = item.getItemId().getValue();
        dto.itemName = item.getItemName();
        dto.category = item.getCategory();
        dto.quantity = item.getQuantity().getValue();
        dto.unitPrice = item.getUnitPrice();
        dto.minimumQuantity = item.getMinimumQuantity();
        return dto;
    }

    public static InventoryItem toDomain(InventoryItemDto dto) {
        return new InventoryItem(
                new ItemId(dto.getItemId()),
                dto.getItemName(),
                dto.getCategory(),
                new Quantity(dto.getQuantity()),
                dto.getUnitPrice(),
                dto.getMinimumQuantity()
        );
    }
}
