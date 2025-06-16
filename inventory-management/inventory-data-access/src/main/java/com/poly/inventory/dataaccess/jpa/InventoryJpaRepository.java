package com.poly.inventory.dataaccess.jpa;

import com.poly.inventory.dataaccess.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Integer> {
    List<InventoryEntity> findByItemNameContainingIgnoreCase(String name);
}
