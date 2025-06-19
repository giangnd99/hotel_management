package com.poly.inventory.application.port.in;

import com.poly.inventory.application.dto.TransactionDto;

import java.time.LocalDate;
import java.util.List;

public interface TransactionUseCase {
    List<TransactionDto> getAllTransactions();

    TransactionDto stockIn(TransactionDto dto);

    TransactionDto stockOut(TransactionDto dto);

    TransactionDto inventoryCheck(TransactionDto dto);

    List<TransactionDto> getReport(LocalDate from, LocalDate to);

    String exportReceipt(int index);
}
