package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.TransactionDto;

import java.util.List;

public interface GetTransactionsHandler {
    List<TransactionDto> handle();
}
