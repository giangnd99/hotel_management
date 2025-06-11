package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.domain.value_object.ItemId;

import java.util.Optional;

public interface UpdateItemHandler {
    Optional<InventoryItemDto> update(ItemId id, InventoryItemDto updatedItem);
}
