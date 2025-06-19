package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.handler.GetReportHandler;
import com.poly.inventory.application.port.out.LoadTransactionPort;
import com.poly.inventory.domain.entity.InventoryTransaction;

import java.time.LocalDate;
import java.util.List;

import static com.poly.inventory.application.mapper.TransactionDtoMapper.toDtoList;

public class GetReportHandlerImpl implements GetReportHandler {

    private final LoadTransactionPort loadTransactionPort;

    public GetReportHandlerImpl(LoadTransactionPort loadTransactionPort) {
        this.loadTransactionPort = loadTransactionPort;
    }

    @Override
    public List<TransactionDto> handle(LocalDate from, LocalDate to) {
        List<InventoryTransaction> transactions = loadTransactionPort.findByDateRange(from, to);
        return toDtoList(transactions);
    }
}
