package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.handler.InventoryCheckHandler;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.application.port.out.SaveTransactionPort;
import com.poly.inventory.domain.value_object.Quantity;

import java.time.LocalDateTime;

import static com.poly.inventory.application.mapper.TransactionDtoMapper.toDomain;
import static com.poly.inventory.application.mapper.TransactionDtoMapper.toDto;

public class InventoryCheckHandlerImpl implements InventoryCheckHandler {

    private final SaveTransactionPort saveTransactionPort;
    private final LoadInventoryPort loadInventoryPort;
    private final SaveInventoryPort saveInventoryPort;

    public InventoryCheckHandlerImpl(SaveTransactionPort saveTransactionPort, LoadInventoryPort loadInventoryPort, SaveInventoryPort saveInventoryPort) {
        this.saveTransactionPort = saveTransactionPort;
        this.loadInventoryPort = loadInventoryPort;
        this.saveInventoryPort = saveInventoryPort;
    }

    @Override
    public TransactionDto handle(TransactionDto dto) {
        var item = loadInventoryPort.loadItemById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        int diff = dto.getQuantity() - item.getQuantity().getValue();
        item.setQuantity(new Quantity(dto.getQuantity()));

        saveInventoryPort.save(item);

        dto.setTransactionType("CHECK");
        dto.setTransactionDate(LocalDateTime.now());

        var domain = saveTransactionPort.save(toDomain(dto));
        return toDto(domain);
    }
}
