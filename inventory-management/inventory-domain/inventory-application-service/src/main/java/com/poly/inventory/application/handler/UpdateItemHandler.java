package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.InventoryItemDto;

import java.util.Optional;

public interface UpdateItemHandler {
    Optional<InventoryItemDto> update(Integer id, InventoryItemDto updatedItem);
}
