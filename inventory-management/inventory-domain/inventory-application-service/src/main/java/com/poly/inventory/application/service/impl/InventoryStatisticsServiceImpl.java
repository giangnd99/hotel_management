package com.poly.inventory.application.service.impl;

import org.springframework.stereotype.Service;

import com.poly.inventory.application.dto.InventoryStatisticsDto;
import com.poly.inventory.application.service.InventoryStatisticsService;

@Service
public class InventoryStatisticsServiceImpl implements InventoryStatisticsService {
    @Override
    public InventoryStatisticsDto getInventoryStatistics() {
        return null;
    }

    @Override
    public Long getTotalItems() {
        return 0L;
    }

    @Override
    public Long getInStockCount() {
        return 0L;
    }

    @Override
    public Long getOutOfStockCount() {
        return 0L;
    }
}
