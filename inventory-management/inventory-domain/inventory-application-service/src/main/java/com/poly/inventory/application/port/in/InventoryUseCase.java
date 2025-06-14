package com.poly.inventory.application.port.in;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.ItemId;

import java.util.List;
import java.util.Optional;

public interface InventoryUseCase {
    List<InventoryItem> getAllItems();
    Optional<InventoryItem> getItemById(ItemId id);
    void createItem(InventoryItemDto request);
    void updateItem(ItemId id, InventoryItemDto request);
    void deleteItem(ItemId id);
}
