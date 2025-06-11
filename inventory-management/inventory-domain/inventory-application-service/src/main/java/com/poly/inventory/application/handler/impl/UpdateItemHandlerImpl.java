package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.handler.UpdateItemHandler;
import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.mapper.InventoryDtoMapper;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.ItemId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateItemHandlerImpl implements UpdateItemHandler {

    private final SaveInventoryPort savePort;
    private final LoadInventoryPort loadPort;

    @Override
    public Optional<InventoryItemDto> update(ItemId id, InventoryItemDto dto) {
        return loadPort.loadItemById(id)
                .map(existing -> {
                    InventoryItem updated = new InventoryItem(
                            id, dto.itemName, dto.category, dto.quantity,
                            dto.unitPrice, dto.minimumQuantity
                    );
                    savePort.save(updated);
                    return InventoryDtoMapper.toDto(updated);
                });
    }
}
