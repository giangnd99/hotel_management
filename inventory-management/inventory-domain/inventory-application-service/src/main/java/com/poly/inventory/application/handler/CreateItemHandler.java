package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.InventoryItemDto;

public interface CreateItemHandler {
    InventoryItemDto create(InventoryItemDto itemDto);
}
