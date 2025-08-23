package com.poly.inventory.dataaccess.jpa;

import com.poly.inventory.dataaccess.entity.InventoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Integer> {
    // Tìm theo tên
    List<InventoryEntity> findByItemNameContainingIgnoreCase(String name, Pageable pageable);

//    // Tìm theo danh mục
//    List<InventoryEntity> findByCategoryId(Long categoryId, Pageable pageable);
//
//    // Tìm theo supplier
//    List<InventoryEntity> findBySupplierId(Long supplierId, Pageable pageable);
//
//    // Tìm theo status
//    List<InventoryEntity> findByStatus(String status, Pageable pageable);
//
//    // Tìm theo quantity range
//    List<InventoryEntity> findByQuantityBetween(Integer min, Integer max, Pageable pageable);
//
//    // Tìm theo price range
//    List<InventoryEntity> findByUnitPriceBetween(Double min, Double max, Pageable pageable);
//
//    // ===== Statistics =====
//    @Query("SELECT COUNT(i) FROM InventoryEntity i")
//    Long getTotalItems();
//
//    @Query("SELECT COUNT(i) FROM InventoryEntity i WHERE i.quantity > 0")
//    Long getInStockCount();
//
//    @Query("SELECT COUNT(i) FROM InventoryEntity i WHERE i.quantity = 0")
//    Long getOutOfStockCount();
//
//    @Query("SELECT SUM(i.quantity * i.unitPrice) FROM InventoryEntity i")
//    Double getTotalStockValue();
}
