package com.poly.inventory.application.service.impl;

import org.springframework.stereotype.Service;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.service.InventorySearchService;

import java.util.List;

@Service
public class InventorySearchServiceImpl implements InventorySearchService {
    @Override
    public List<InventoryItemDto> searchItemsByName(String name) {
        return List.of();
    }

    @Override
    public List<InventoryItemDto> searchItems(String name, String code, String category, String supplier, int page, int size) {
        return List.of();
    }

    @Override
    public List<InventoryItemDto> filterByCategory(Long categoryId, int page, int size) {
        return List.of();
    }

    @Override
    public List<InventoryItemDto> filterBySupplier(Long supplierId, int page, int size) {
        return List.of();
    }

    @Override
    public List<InventoryItemDto> filterByStatus(String status, int page, int size) {
        return List.of();
    }

    @Override
    public List<InventoryItemDto> filterByQuantityRange(Integer min, Integer max, int page, int size) {
        return List.of();
    }

    @Override
    public List<InventoryItemDto> filterByPriceRange(Double min, Double max, int page, int size) {
        return List.of();
    }
}
