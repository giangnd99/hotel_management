package com.poly.inventory.application.port.out;

import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.ItemId;

import java.util.List;
import java.util.Optional;

public interface LoadInventoryPort {
    List<InventoryItem> loadAllItems();

    Optional<InventoryItem> loadItemById(ItemId id);
}
