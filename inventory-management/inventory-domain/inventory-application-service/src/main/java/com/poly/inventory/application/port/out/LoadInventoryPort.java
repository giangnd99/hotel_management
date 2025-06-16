package com.poly.inventory.application.port.out;

import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface LoadInventoryPort {
    List<InventoryItem> loadAllItems();

    Optional<InventoryItem> loadItemById(Integer id);

    List<InventoryItem> searchByName(String name);
}
