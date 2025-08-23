package com.poly.inventory.application.service;

import com.poly.inventory.application.dto.InventoryItemDto;

import java.util.List;

public interface InventorySearchService {
    List<InventoryItemDto> searchItemsByName(String name);

    List<InventoryItemDto> searchItems(String name, String code, String category, String supplier, int page, int size);

    List<InventoryItemDto> filterByCategory(Long categoryId, int page, int size);
    List<InventoryItemDto> filterBySupplier(Long supplierId, int page, int size);
    List<InventoryItemDto> filterByStatus(String status, int page, int size);
    List<InventoryItemDto> filterByQuantityRange(Integer min, Integer max, int page, int size);
    List<InventoryItemDto> filterByPriceRange(Double min, Double max, int page, int size);
}
