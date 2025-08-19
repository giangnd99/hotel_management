package com.poly.inventory.application.service.impl;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.application.port.out.DeleteInventoryPort;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.application.service.InventoryCrudService;
import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;
import java.util.Optional;

public class InventoryCrudServiceImpl implements InventoryCrudService {
    private final LoadInventoryPort loadPort;
    private final SaveInventoryPort savePort;
    private final DeleteInventoryPort deletePort;

    public InventoryCrudServiceImpl(LoadInventoryPort loadPort, SaveInventoryPort savePort, DeleteInventoryPort deletePort) {
        this.loadPort = loadPort;
        this.savePort = savePort;
        this.deletePort = deletePort;
    }

    @Override
    public List<InventoryItemDto> getAllItems() {
        List<InventoryItem> domainItems = loadPort.loadAllItems();
        return InventoryDtoMapper.toDtoList(domainItems);
    }

    @Override
    public Optional<InventoryItemDto> getItemById(Integer id) {
        return loadPort.loadItemById(id).map(InventoryDtoMapper::toDto);
    }

    @Override
    public void createItem(InventoryItemDto dto) {
        InventoryItem item = InventoryDtoMapper.toDomain(dto);
        savePort.save(item);
    }

    @Override
    public void updateItem(Integer id, InventoryItemDto dto) {
        loadPort.loadItemById(id)
                .map(existing -> {
                    InventoryItem updated = InventoryDtoMapper.toDomain(dto);
                    savePort.save(updated);
                    return InventoryDtoMapper.toDto(updated);
                });
    }

    @Override
    public void deleteItem(Integer id) {
        deletePort.deleteById(id);
    }
}
