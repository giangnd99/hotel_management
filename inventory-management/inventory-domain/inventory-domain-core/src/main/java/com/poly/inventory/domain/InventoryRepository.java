package com.poly.inventory.domain;

import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    InventoryItem save(InventoryItem item);

    Optional<InventoryItem> findById(Integer id);

    List<InventoryItem> findAll();

    void deleteById(Integer id);
}
