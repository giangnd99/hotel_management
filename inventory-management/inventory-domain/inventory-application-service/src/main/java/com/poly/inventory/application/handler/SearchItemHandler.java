package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.InventoryItemDto;

import java.util.List;

public interface SearchItemHandler {
    List<InventoryItemDto> searchItemsByName(String name);
}
