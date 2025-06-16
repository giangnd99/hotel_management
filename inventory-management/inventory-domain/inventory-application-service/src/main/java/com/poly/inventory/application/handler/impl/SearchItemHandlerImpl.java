package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.handler.SearchItemHandler;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;

public class SearchItemHandlerImpl implements SearchItemHandler {

    private final LoadInventoryPort loadInventoryPort;

    public SearchItemHandlerImpl(LoadInventoryPort loadInventoryPort) {
        this.loadInventoryPort = loadInventoryPort;
    }

    @Override
    public List<InventoryItemDto> searchItemsByName(String name) {
        List<InventoryItem> items = (name == null || name.isEmpty())
                ? loadInventoryPort.loadAllItems()
                : loadInventoryPort.searchByName(name);

        return items.stream()
                .map(InventoryDtoMapper::toDto)
                .toList();
    }
}
