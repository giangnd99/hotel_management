package com.poly.inventory.dataaccess.repo;

import com.poly.inventory.dataaccess.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer> {
}
