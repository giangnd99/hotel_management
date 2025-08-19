package com.poly.inventory.application.port.in;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.dto.InventoryStatisticsDto;

import java.util.List;
import java.util.Optional;

public interface InventoryUseCase {

    // ========== CRUD ==========
    List<InventoryItemDto> getAllItems();

    Optional<InventoryItemDto> getItemById(Integer id);

    void createItem(InventoryItemDto request);

    void updateItem(Integer id, InventoryItemDto request);

    void deleteItem(Integer id);

    List<InventoryItemDto> searchItemsByName(String name);

    // ========== STATISTICS ==========
    InventoryStatisticsDto getInventoryStatistics();

    Long getTotalItems();

    Long getInStockCount();

    Long getOutOfStockCount();

    // ========== SEARCH (multi-criteria) ==========
    List<InventoryItemDto> searchItems(
            String name,
            String code,
            String category,
            String supplier,
            int page,
            int size
    );

    // ========== FILTER ==========
    List<InventoryItemDto> filterByCategory(Long categoryId, int page, int size);

    List<InventoryItemDto> filterBySupplier(Long supplierId, int page, int size);

    List<InventoryItemDto> filterByStatus(String status, int page, int size);

    List<InventoryItemDto> filterByQuantityRange(Integer min, Integer max, int page, int size);

    List<InventoryItemDto> filterByPriceRange(Double min, Double max, int page, int size);
}
