package com.poly.inventory.application.service;

import com.poly.inventory.application.dto.InventoryItemDto;

import java.util.List;
import java.util.Optional;

public interface InventoryCrudService {
    List<InventoryItemDto> getAllItems();
    Optional<InventoryItemDto> getItemById(Integer id);
    void createItem(InventoryItemDto dto);
    void updateItem(Integer id, InventoryItemDto dto);
    void deleteItem(Integer id);
}
