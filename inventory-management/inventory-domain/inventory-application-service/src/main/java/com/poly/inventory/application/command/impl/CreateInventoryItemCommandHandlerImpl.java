package com.poly.inventory.application.command.impl;

import com.poly.inventory.application.command.CreateInventoryItemCommandHandler;
import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.domain.InventoryRepository;
import com.poly.inventory.domain.entity.InventoryItem;
import org.springframework.stereotype.Service;

@Service
public class CreateInventoryItemCommandHandlerImpl implements CreateInventoryItemCommandHandler {

    private final InventoryRepository repository;

    public CreateInventoryItemCommandHandlerImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public InventoryItemDto create(InventoryItemDto dto) {
        InventoryItem item = InventoryDtoMapper.toDomain(dto);
        InventoryItem saved = repository.save(item);
        return InventoryDtoMapper.toDto(saved);
    }
}
