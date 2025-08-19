package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.handler.StockOutHandler;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.application.port.out.SaveTransactionPort;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.Quantity;

import java.time.LocalDateTime;

import static com.poly.inventory.application.mapper.TransactionDtoMapper.toDomain;
import static com.poly.inventory.application.mapper.TransactionDtoMapper.toDto;

public class StockOutHandlerImpl implements StockOutHandler {

    private final SaveTransactionPort saveTransactionPort;
    private final LoadInventoryPort loadInventoryPort;
    private final SaveInventoryPort saveInventoryPort;

    public StockOutHandlerImpl(SaveTransactionPort saveTransactionPort, LoadInventoryPort loadInventoryPort, SaveInventoryPort saveInventoryPort) {
        this.saveTransactionPort = saveTransactionPort;
        this.loadInventoryPort = loadInventoryPort;
        this.saveInventoryPort = saveInventoryPort;
    }

    @Override
    public TransactionDto handle(TransactionDto dto) {
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng xuất phải > 0");
        }

        InventoryItem item = loadInventoryPort.loadItemById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getQuantity().getValue() < dto.getQuantity()) {
            throw new IllegalArgumentException("Không đủ tồn kho");
        }

        item.setQuantity(item.getQuantity().subtract(new Quantity(dto.getQuantity())));
        saveInventoryPort.save(item);

        dto.setTransactionType("EXPORT");
        dto.setTransactionDate(LocalDateTime.now());

        var domain = saveTransactionPort.save(toDomain(dto));
        return toDto(domain);
    }
}
