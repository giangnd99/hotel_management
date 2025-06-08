package com.poly.inventory.application.port.out;
import com.poly.inventory.domain.model.entity.InventoryItem;

public interface SaveInventoryPort {
    void save(InventoryItem item);
}
