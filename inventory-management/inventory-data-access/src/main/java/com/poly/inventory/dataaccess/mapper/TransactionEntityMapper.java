package com.poly.inventory.dataaccess.mapper;

import com.poly.inventory.dataaccess.entity.TransactionEntity;
import com.poly.inventory.domain.entity.InventoryTransaction;
import com.poly.inventory.domain.value_object.ItemId;
import com.poly.inventory.domain.value_object.Quantity;
import com.poly.inventory.domain.value_object.TransactionId;
import com.poly.inventory.domain.value_object.TransactionType;

public class TransactionEntityMapper {

    public static InventoryTransaction toDomain(TransactionEntity entity) {
        if (entity == null) return null;

        return new InventoryTransaction(
                new TransactionId(entity.getId()),
                new ItemId(entity.getItemId()),
                entity.getStaffId(),
                TransactionType.valueOf(entity.getTransactionType()),
                new Quantity(entity.getQuantity()),
                entity.getTransactionDate()
        );
    }

    public static TransactionEntity toEntity(InventoryTransaction transaction) {
        if (transaction == null) return null;

        TransactionEntity entity = new TransactionEntity();
        entity.setId(transaction.getTransactionId().getValue());
        entity.setItemId(transaction.getItemId().getValue());
        entity.setStaffId(transaction.getStaffId());
        entity.setTransactionType(transaction.getTransactionType().name());
        entity.setQuantity(transaction.getQuantity().getValue());
        entity.setTransactionDate(transaction.getTransactionDate());
        return entity;
    }
}
