package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.handler.GetItemsHandler;
import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;

public class GetItemsHandlerImpl implements GetItemsHandler {
    private final LoadInventoryPort loadInventoryPort;

    public GetItemsHandlerImpl(LoadInventoryPort loadInventoryPort) {
        this.loadInventoryPort = loadInventoryPort;
    }

    @Override
    public List<InventoryItemDto> getAllItems() {
        List<InventoryItem> domainItems = loadInventoryPort.loadAllItems();
        return InventoryDtoMapper.toDtoList(domainItems);
    }
}
