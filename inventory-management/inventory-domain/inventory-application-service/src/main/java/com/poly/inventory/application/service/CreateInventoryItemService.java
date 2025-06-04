package com.poly.inventory.application.service;
import com.poly.inventory.application.command.CreateInventoryItemCommand;
import com.poly.inventory.application.port.in.CreateInventoryItemUseCase;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.domain.entity.InventoryItem;
import org.springframework.stereotype.Service;

@Service
public class CreateInventoryItemService implements CreateInventoryItemUseCase {

    private final SaveInventoryPort saveInventoryPort;

    public CreateInventoryItemService(SaveInventoryPort saveInventoryPort) {
        this.saveInventoryPort = saveInventoryPort;
    }

    @Override
    public InventoryItem create(CreateInventoryItemCommand command) {
        InventoryItem item = new InventoryItem(
                null,
                command.getItemName(),
                command.getCategory(),
                command.getQuantity(),
                command.getUnitPrice(),
                command.getMinimumQuantity()
        );

        saveInventoryPort.save(item);
        return item;
    }
}