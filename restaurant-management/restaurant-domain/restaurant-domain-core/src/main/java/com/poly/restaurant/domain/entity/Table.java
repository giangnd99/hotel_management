package com.poly.restaurant.domain.entity;

public class Table {
    private final String id;
    private int number;
    private TableStatus status;

    public Table(String id, int number, TableStatus status) {
        if (id == null || id.isEmpty()) throw new IllegalArgumentException("Id must not be empty");
        if (number <= 0) throw new IllegalArgumentException("Table number must be positive");
        if (status == null) throw new IllegalArgumentException("Status must not be null");
        this.id = id;
        this.number = number;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
} 