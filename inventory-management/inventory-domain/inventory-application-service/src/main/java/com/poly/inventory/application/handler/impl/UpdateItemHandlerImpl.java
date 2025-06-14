package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.handler.UpdateItemHandler;
import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.domain.entity.InventoryItem;

import java.util.Optional;

public class UpdateItemHandlerImpl implements UpdateItemHandler {

    private final SaveInventoryPort savePort;
    private final LoadInventoryPort loadPort;

    public UpdateItemHandlerImpl(SaveInventoryPort savePort, LoadInventoryPort loadPort) {
        this.savePort = savePort;
        this.loadPort = loadPort;
    }

    @Override
    public Optional<InventoryItemDto> update(Integer id, InventoryItemDto dto) {
        return loadPort.loadItemById(id)
                .map(existing -> {
                    InventoryItem updated = InventoryDtoMapper.toDomain(dto);
                    savePort.save(updated);
                    return InventoryDtoMapper.toDto(updated);
                });
    }
}
