package com.poly.inventory.application.port.in.impl;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.dto.InventoryStatisticsDto;
import com.poly.inventory.application.handler.*;
import com.poly.inventory.application.port.in.InventoryUseCase;
import com.poly.inventory.application.service.InventoryCrudService;
import com.poly.inventory.application.service.InventorySearchService;
import com.poly.inventory.application.service.InventoryStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryUseCaseImpl implements InventoryUseCase {

    private final InventoryCrudService crudService;
    private final InventorySearchService searchService;
    private final InventoryStatisticsService statisticsService;

    // CRUD
    @Override
    public List<InventoryItemDto> getAllItems() {
        return crudService.getAllItems();
    }

    @Override
    public Optional<InventoryItemDto> getItemById(Integer id) {
        return crudService.getItemById(id);
    }

    @Override
    public void createItem(InventoryItemDto request) {
        crudService.createItem(request);
    }

    @Override
    public void updateItem(Integer id, InventoryItemDto request) {
        crudService.updateItem(id, request);
    }

    @Override
    public void deleteItem(Integer id) {
        crudService.deleteItem(id);
    }

    @Override
    public List<InventoryItemDto> searchItemsByName(String name) {
        return searchService.searchItemsByName(name);
    }

    // Statistics
    @Override
    public InventoryStatisticsDto getInventoryStatistics() {
        return statisticsService.getInventoryStatistics();
    }

    @Override
    public Long getTotalItems() {
        return statisticsService.getTotalItems();
    }

    @Override
    public Long getInStockCount() {
        return statisticsService.getInStockCount();
    }

    @Override
    public Long getOutOfStockCount() {
        return statisticsService.getOutOfStockCount();
    }

    // Search & Filter
    @Override
    public List<InventoryItemDto> searchItems(String name, String code, String category, String supplier, int page, int size) {
        return searchService.searchItems(name, code, category, supplier, page, size);
    }

    @Override
    public List<InventoryItemDto> filterByCategory(Long categoryId, int page, int size) {
        return searchService.filterByCategory(categoryId, page, size);
    }

    @Override
    public List<InventoryItemDto> filterBySupplier(Long supplierId, int page, int size) {
        return searchService.filterBySupplier(supplierId, page, size);
    }

    @Override
    public List<InventoryItemDto> filterByStatus(String status, int page, int size) {
        return searchService.filterByStatus(status, page, size);
    }

    @Override
    public List<InventoryItemDto> filterByQuantityRange(Integer min, Integer max, int page, int size) {
        return searchService.filterByQuantityRange(min, max, page, size);
    }

    @Override
    public List<InventoryItemDto> filterByPriceRange(Double min, Double max, int page, int size) {
        return searchService.filterByPriceRange(min, max, page, size);
    }
}
