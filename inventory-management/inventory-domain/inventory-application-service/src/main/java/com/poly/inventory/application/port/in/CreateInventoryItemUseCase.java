package com.poly.inventory.application.port.in;
import com.poly.inventory.application.command.CreateInventoryItemCommand;
import com.poly.inventory.domain.model.entity.InventoryItem;

@FunctionalInterface
public interface CreateInventoryItemUseCase {
    InventoryItem create(CreateInventoryItemCommand command);
}
