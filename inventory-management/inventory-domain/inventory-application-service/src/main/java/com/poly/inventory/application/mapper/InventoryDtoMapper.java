package com.poly.inventory.application.mapper;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.domain.entity.InventoryItem;

public class InventoryDtoMapper {
    public static InventoryItemDto toDto(InventoryItem item) {
        InventoryItemDto dto = new InventoryItemDto();
        dto.itemId = item.getItemId();
        dto.itemName = item.getItemName();
        dto.category = item.getCategory();
        dto.quantity = item.getQuantity();
        dto.unitPrice = item.getUnitPrice();
        dto.minimumQuantity = item.getMinimumQuantity();
        return dto;
    }

    public static InventoryItem toDomain(InventoryItemDto dto) {
        return new InventoryItem(
                dto.getItemId(),
                dto.getItemName(),
                dto.getCategory(),
                dto.getQuantity(),
                dto.getUnitPrice(),
                dto.getMinimumQuantity()
        );
    }
}
