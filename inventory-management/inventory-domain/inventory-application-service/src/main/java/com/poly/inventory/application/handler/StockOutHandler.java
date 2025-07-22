package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.TransactionDto;

public interface StockOutHandler {
    TransactionDto handle(TransactionDto dto);
}
