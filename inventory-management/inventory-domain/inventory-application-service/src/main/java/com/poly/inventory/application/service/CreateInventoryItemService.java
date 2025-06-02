package com.poly.inventory.application.service;
import com.poly.inventory.application.command.CreateInventoryItemCommand;
import com.poly.inventory.application.port.in.CreateInventoryItemUseCase;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.domain.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateInventoryItemService implements CreateInventoryItemUseCase {

    private final SaveInventoryPort saveInventoryPort;

    @Override
    public InventoryItem create(CreateInventoryItemCommand command) {
        InventoryItem item = new InventoryItem(command.getName(), command.getQuantity());
        saveInventoryPort.save(item);
        return item;
    }
}