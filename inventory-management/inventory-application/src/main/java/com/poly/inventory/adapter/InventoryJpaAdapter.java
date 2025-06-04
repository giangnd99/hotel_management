package com.poly.inventory.adapter;

import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.entity.InventoryEntity;
import com.poly.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventoryJpaAdapter implements LoadInventoryPort, SaveInventoryPort {

    private final InventoryRepository inventoryRepository;

    public InventoryJpaAdapter(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<InventoryItem> loadAllItems() {
        return inventoryRepository.findAll().stream()
                .map(entity -> new InventoryItem(
                        entity.getItemId(),
                        entity.getItemName(),
                        entity.getCategory(),
                        entity.getQuantity(),
                        entity.getUnitPrice(),
                        entity.getMinimumQuantity()
                ))
                .toList();
    }

    @Override
    public Optional<InventoryItem> loadItemById(Integer id) {
        return inventoryRepository.findById(id)
                .map(entity -> new InventoryItem(
                        entity.getItemId(),
                        entity.getItemName(),
                        entity.getCategory(),
                        entity.getQuantity(),
                        entity.getUnitPrice(),
                        entity.getMinimumQuantity()
                ));
    }

    @Override
    public void save(InventoryItem item) {
        InventoryEntity entity = new InventoryEntity();
        entity.setItemName(item.getItemName());
        entity.setCategory(item.getCategory());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setMinimumQuantity(item.getMinimumQuantity());
        inventoryRepository.save(entity);
    }
}
