package com.poly.inventory.application.handler;

import com.poly.inventory.application.dto.TransactionDto;

import java.time.LocalDate;
import java.util.List;

public interface GetReportHandler {
    List<TransactionDto> handle(LocalDate from, LocalDate to);
}
