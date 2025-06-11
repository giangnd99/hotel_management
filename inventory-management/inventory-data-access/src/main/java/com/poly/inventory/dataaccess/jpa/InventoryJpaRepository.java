package com.poly.inventory.dataaccess.jpa;

import com.poly.inventory.dataaccess.entity.InventoryEntity;
import com.poly.inventory.domain.value_object.ItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, ItemId> {
}
