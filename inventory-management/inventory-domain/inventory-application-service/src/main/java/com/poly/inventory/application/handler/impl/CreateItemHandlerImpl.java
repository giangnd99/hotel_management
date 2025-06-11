package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.handler.CreateItemHandler;
import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.domain.entity.InventoryItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateItemHandlerImpl implements CreateItemHandler {

    private final SaveInventoryPort savePort;

    @Override
    public InventoryItemDto create(InventoryItemDto dto) {
        InventoryItem item = InventoryDtoMapper.toDomain(dto);
        savePort.save(item);
        return InventoryDtoMapper.toDto(item);
    }
}
