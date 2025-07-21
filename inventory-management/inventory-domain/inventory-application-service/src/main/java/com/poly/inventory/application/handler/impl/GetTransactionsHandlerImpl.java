package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.handler.GetTransactionsHandler;
import com.poly.inventory.application.mapper.TransactionDtoMapper;
import com.poly.inventory.application.port.out.LoadTransactionPort;
import com.poly.inventory.domain.entity.InventoryTransaction;

import java.util.List;

public class GetTransactionsHandlerImpl implements GetTransactionsHandler {
    private final LoadTransactionPort loadTransactionPort;

    public GetTransactionsHandlerImpl(LoadTransactionPort loadTransactionPort) {
        this.loadTransactionPort = loadTransactionPort;
    }

    @Override
    public List<TransactionDto> handle() {
        List<InventoryTransaction> transactions = loadTransactionPort.findAll();
        return transactions.stream()
                .map(TransactionDtoMapper::toDto)
                .toList();
    }
}
