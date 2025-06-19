package com.poly.inventory.domain.entity;

import com.poly.inventory.domain.value_object.ItemId;
import com.poly.inventory.domain.value_object.Quantity;
import com.poly.inventory.domain.value_object.TransactionId;

import java.time.LocalDateTime;
import java.util.Objects;

public class InventoryTransaction {
    private final TransactionId transactionId;
    private final ItemId itemId;
    private final Integer staffId;
    private final String transactionType;
    private final Quantity quantity;
    private final LocalDateTime transactionDate;

    public InventoryTransaction(TransactionId transactionId, ItemId itemId, Integer staffId, String transactionType, Quantity quantity, LocalDateTime transactionDate) {
        this.transactionId = transactionId;
        this.itemId = itemId;
        this.staffId = staffId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public ItemId getItemId() {
        return itemId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryTransaction)) return false;
        InventoryTransaction that = (InventoryTransaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "InventoryTransaction{" +
                "transactionId=" + transactionId +
                ", itemId=" + itemId +
                ", staffId=" + staffId +
                ", transactionType='" + transactionType + '\'' +
                ", quantity=" + quantity +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
