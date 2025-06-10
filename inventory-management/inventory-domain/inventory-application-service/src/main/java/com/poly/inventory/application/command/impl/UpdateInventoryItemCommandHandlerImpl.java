package com.poly.inventory.application.command.impl;

import com.poly.inventory.application.command.UpdateInventoryItemCommandHandler;
import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.domain.InventoryRepository;
import com.poly.inventory.domain.entity.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateInventoryItemCommandHandlerImpl implements UpdateInventoryItemCommandHandler {

    private final InventoryRepository repository;

    public UpdateInventoryItemCommandHandlerImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<InventoryItemDto> update(Integer id, InventoryItemDto dto) {
        return repository.findById(id)
                .map(existing -> {
                    InventoryItem updated = new InventoryItem(
                            id,
                            dto.itemName,
                            dto.category,
                            dto.quantity,
                            dto.unitPrice,
                            dto.minimumQuantity
                    );
                    InventoryItem saved = repository.save(updated);
                    return InventoryDtoMapper.toDto(saved);
                });
    }
}
