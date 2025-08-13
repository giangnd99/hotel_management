package com.poly.restaurant.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Order {
    private final String id;
    private final String customerId;
    private final String tableId;
    private final List<OrderItem> items;
    private final LocalDateTime createdAt;
    private OrderStatus status;
    private String customerNote;

    public Order(String id, String customerId, String tableId, List<OrderItem> items, LocalDateTime createdAt) {
        if (id == null || id.trim().isEmpty()) throw new IllegalArgumentException("Id must not be empty");
        if (customerId == null || customerId.trim().isEmpty())
            throw new IllegalArgumentException("CustomerId must not be empty");
        if (tableId == null || tableId.trim().isEmpty())
            throw new IllegalArgumentException("TableId must not be empty");
        if (items == null || items.isEmpty()) throw new IllegalArgumentException("Order must have at least one item");
        if (createdAt == null) throw new IllegalArgumentException("CreatedAt must not be null");

        this.id = id.trim();
        this.customerId = customerId.trim();
        this.tableId = tableId.trim();
        this.items = new ArrayList<>(items);
        this.createdAt = createdAt;
        this.status = OrderStatus.NEW;
    }

    public String getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getTableId() {
        return tableId;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getCustomerNote() {
        return customerNote;
    }


    // Business methods with validation
    public void setStatus(OrderStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Status cannot be null");

        // Validate status transition
        if (!isValidStatusTransition(status, newStatus)) {
            throw new IllegalStateException("Invalid status transition from " + status + " to " + newStatus);
        }

        this.status = newStatus;
    }

    public void setCustomerNote(String note) {
        this.customerNote = note != null ? note.trim() : null;
    }

    public void addItem(OrderItem item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        if (status != OrderStatus.NEW)
            throw new IllegalStateException("Cannot add items to order that is not in NEW status");

        items.add(item);
    }

    public void removeItem(int index) {
        if (index < 0 || index >= items.size()) throw new IllegalArgumentException("Invalid item index");
        if (status != OrderStatus.NEW)
            throw new IllegalStateException("Cannot remove items from order that is not in NEW status");

        items.remove(index);
    }

    private boolean isValidStatusTransition(OrderStatus current, OrderStatus newStatus) {
        switch (current) {
            case NEW:
                return newStatus == OrderStatus.IN_PROGRESS || newStatus == OrderStatus.CANCELLED;
            case IN_PROGRESS:
                return newStatus == OrderStatus.COMPLETED || newStatus == OrderStatus.CANCELLED;
            case COMPLETED:
            case CANCELLED:
                return false; // Terminal states
            default:
                return false;
        }
    }

    public boolean canBeModified() {
        return status == OrderStatus.NEW;
    }

    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }
}
