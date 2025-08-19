package com.poly.inventory.application.service;

import com.poly.inventory.application.dto.InventoryStatisticsDto;

public interface InventoryStatisticsService {
    InventoryStatisticsDto getInventoryStatistics();
    Long getTotalItems();
    Long getInStockCount();
    Long getOutOfStockCount();
}

