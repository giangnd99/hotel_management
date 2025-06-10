package com.poly.inventory.application.command;

import com.poly.inventory.application.dto.InventoryItemDto;

import java.util.Optional;

public interface UpdateInventoryItemCommandHandler {
    Optional<InventoryItemDto> update(Integer id, InventoryItemDto updatedItem);
}
