package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.TransactionDto;

public interface InventoryCheckHandler {
    TransactionDto handle(TransactionDto dto);
}
