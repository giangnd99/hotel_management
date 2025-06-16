package com.poly.inventory.application.port.in;

import com.poly.inventory.application.dto.InventoryItemDto;

import java.util.List;
import java.util.Optional;

public interface InventoryUseCase {
    List<InventoryItemDto> getAllItems();

    Optional<InventoryItemDto> getItemById(Integer id);

    void createItem(InventoryItemDto request);

    void updateItem(Integer id, InventoryItemDto request);

    void deleteItem(Integer id);
}
