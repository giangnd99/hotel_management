package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.TransactionDto;

public interface StockInHandler {
    TransactionDto handle(TransactionDto dto);
}
