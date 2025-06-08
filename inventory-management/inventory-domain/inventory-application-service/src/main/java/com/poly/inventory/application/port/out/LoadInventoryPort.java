package com.poly.inventory.application.port.out;

import com.poly.inventory.domain.model.entity.InventoryItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface LoadInventoryPort {
    List<InventoryItem> loadAllItems();
    Optional<InventoryItem> loadItemById(Integer id);
}
