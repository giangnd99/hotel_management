package com.poly.inventory.application.port.out;

import com.poly.inventory.domain.entity.InventoryItem;

public interface SaveInventoryPort {
    void save(InventoryItem item);
}
