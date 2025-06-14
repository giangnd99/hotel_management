package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.handler.GetItemByIdHandler;

import java.util.Optional;

public class GetItemByIdHandlerImpl implements GetItemByIdHandler {

    private final LoadInventoryPort loadInventoryPort;

    public GetItemByIdHandlerImpl(LoadInventoryPort loadInventoryPort) {
        this.loadInventoryPort = loadInventoryPort;
    }

    @Override
    public Optional<InventoryItemDto> getItemById(Integer id) {
        return loadInventoryPort.loadItemById(id).map(InventoryDtoMapper::toDto);
    }
}
