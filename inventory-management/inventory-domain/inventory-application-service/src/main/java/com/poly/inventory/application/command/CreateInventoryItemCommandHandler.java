package com.poly.inventory.application.command;

import com.poly.inventory.application.dto.InventoryItemDto;

public interface CreateInventoryItemCommandHandler {
    InventoryItemDto create(InventoryItemDto itemDto);
}
