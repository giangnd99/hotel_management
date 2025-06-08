package com.poly.inventory.dataaccess.adapter;

import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.dataaccess.entity.InventoryEntity;
import com.poly.inventory.dataaccess.mapper.InventoryMapper;
import com.poly.inventory.dataaccess.repo.InventoryRepository;
import com.poly.inventory.domain.model.entity.InventoryItem;
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
                .map(InventoryMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<InventoryItem> loadItemById(Integer id) {
        return inventoryRepository.findById(id)
                .map(InventoryMapper::toDomain);
    }

    @Override
    public void save(InventoryItem item) {
        InventoryEntity entity = InventoryMapper.toEntity(item);
        inventoryRepository.save(entity);
    }
}
