package com.poly.inventory.application.port.in;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryUseCase {
    List<InventoryItem> getAllItems();

    Optional<InventoryItem> getItemById(Integer id);

    void createItem(InventoryItemDto request);

    void updateItem(Integer id, InventoryItemDto request);

    void deleteItem(Integer id);
}
