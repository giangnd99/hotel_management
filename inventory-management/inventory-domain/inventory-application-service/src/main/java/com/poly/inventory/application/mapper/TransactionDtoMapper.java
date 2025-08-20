package com.poly.inventory.application.mapper;

import com.poly.inventory.application.dto.TransactionDto;
import com.poly.inventory.domain.entity.InventoryTransaction;
import com.poly.inventory.domain.value_object.ItemId;
import com.poly.inventory.domain.value_object.Quantity;
import com.poly.inventory.domain.value_object.TransactionId;
import com.poly.inventory.domain.value_object.TransactionType;

import java.util.List;

public class TransactionDtoMapper {

    public static TransactionDto toDto(InventoryTransaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setTransactionId(transaction.getTransactionId().getValue());
        dto.setItemId(transaction.getItemId().getValue());
        dto.setStaffId(transaction.getStaffId());
        dto.setTransactionType(transaction.getTransactionType().name());
        dto.setQuantity(transaction.getQuantity().getValue());
        dto.setTransactionDate(transaction.getTransactionDate());
        return dto;
    }

    public static InventoryTransaction toDomain(TransactionDto dto) {
        return new InventoryTransaction(
                new TransactionId(dto.getTransactionId()),
                new ItemId(dto.getItemId()),
                dto.getStaffId(),
                TransactionType.valueOf(dto.getTransactionType()),
                new Quantity(dto.getQuantity()),
                dto.getTransactionDate()
        );
    }

    public static List<TransactionDto> toDtoList(List<InventoryTransaction> transactions) {
        return transactions.stream()
                .map(TransactionDtoMapper::toDto)
                .toList();
    }

    public static List<InventoryTransaction> toDomainList(List<TransactionDto> dtoTransactions) {
        return dtoTransactions.stream()
                .map(TransactionDtoMapper::toDomain)
                .toList();
    }
}
