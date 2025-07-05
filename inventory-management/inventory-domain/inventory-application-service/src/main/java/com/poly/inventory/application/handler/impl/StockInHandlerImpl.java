package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.handler.StockInHandler;
import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.port.out.SaveInventoryPort;
import com.poly.inventory.application.port.out.SaveTransactionPort;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.Quantity;

import java.time.LocalDateTime;

import static com.poly.inventory.application.mapper.TransactionDtoMapper.toDomain;
import static com.poly.inventory.application.mapper.TransactionDtoMapper.toDto;

public class StockInHandlerImpl implements StockInHandler {

    private final SaveTransactionPort saveTransactionPort;
    private final LoadInventoryPort loadInventoryPort;
    private final SaveInventoryPort saveInventoryPort;

    public StockInHandlerImpl(SaveTransactionPort saveTransactionPort, LoadInventoryPort loadInventoryPort, SaveInventoryPort saveInventoryPort) {
        this.saveTransactionPort = saveTransactionPort;
        this.loadInventoryPort = loadInventoryPort;
        this.saveInventoryPort = saveInventoryPort;
    }

    @Override
    public TransactionDto handle(TransactionDto dto) {
        InventoryItem item = loadInventoryPort.loadItemById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

//        var staff = staffServiceClient.getStaffById(dto.getStaffId()).getBody();
//        if (staff == null) {
//            throw new RuntimeException("Staff not found");
//        }

        // quantity: Quantity + int
        item.setQuantity(item.getQuantity().add(new Quantity(dto.getQuantity())));
        saveInventoryPort.save(item);

        dto.setTransactionType("IN");
        dto.setTransactionDate(LocalDateTime.now());

        var domain = saveTransactionPort.save(toDomain(dto));
        return toDto(domain);
    }
}
