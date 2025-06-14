package com.poly.inventory.dataaccess.adapter;

import com.poly.inventory.application.port.out.DeleteInventoryPort;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.dataaccess.entity.InventoryEntity;
import com.poly.inventory.dataaccess.mapper.InventoryEntityMapper;
import com.poly.inventory.dataaccess.jpa.InventoryJpaRepository;
import com.poly.inventory.domain.entity.InventoryItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventoryJpaAdapter implements LoadInventoryPort, SaveInventoryPort, DeleteInventoryPort {

    private final InventoryJpaRepository inventoryRepository;

    public InventoryJpaAdapter(InventoryJpaRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public List<InventoryItem> loadAllItems() {
        return inventoryRepository.findAll().stream()
                .map(InventoryEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<InventoryItem> loadItemById(Integer id) {
        return inventoryRepository.findById(id)
                .map(InventoryEntityMapper::toDomain);
    }

    @Override
    public void save(InventoryItem item) {
        InventoryEntity entity = InventoryEntityMapper.toEntity(item);
        inventoryRepository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        inventoryRepository.deleteById(id);
    }
}
