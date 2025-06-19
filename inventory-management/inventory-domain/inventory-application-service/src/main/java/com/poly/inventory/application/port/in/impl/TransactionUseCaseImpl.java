package com.poly.inventory.application.port.in.impl;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.application.handler.*;
import com.poly.inventory.application.port.in.TransactionUseCase;

import java.time.LocalDate;
import java.util.List;

public class TransactionUseCaseImpl implements TransactionUseCase {
    private final GetTransactionsHandler getTransactionsHandler;
    private final StockInHandler stockInHandler;
    private final StockOutHandler stockOutHandler;
    private final InventoryCheckHandler inventoryCheckHandler;
    private final GetReportHandler getReportHandler;

    public TransactionUseCaseImpl(
            GetTransactionsHandler getTransactionsHandler,
            StockInHandler stockInHandler,
            StockOutHandler stockOutHandler,
            InventoryCheckHandler inventoryCheckHandler,
            GetReportHandler getReportHandler
    ) {
        this.getTransactionsHandler = getTransactionsHandler;
        this.stockInHandler = stockInHandler;
        this.stockOutHandler = stockOutHandler;
        this.inventoryCheckHandler = inventoryCheckHandler;
        this.getReportHandler = getReportHandler;
    }


    @Override
    public List<TransactionDto> getAllTransactions() {
        return getTransactionsHandler.handle();
    }

    @Override
    public TransactionDto stockIn(TransactionDto dto) {
        return stockInHandler.handle(dto);
    }

    @Override
    public TransactionDto stockOut(TransactionDto dto) {
        return stockOutHandler.handle(dto);
    }

    @Override
    public TransactionDto inventoryCheck(TransactionDto dto) {
        return inventoryCheckHandler.handle(dto);
    }

    @Override
    public List<TransactionDto> getReport(LocalDate from, LocalDate to) {
        return getReportHandler.handle(from, to);
    }

    @Override
    public String exportReceipt(int index) {
        return "";
    }
}
