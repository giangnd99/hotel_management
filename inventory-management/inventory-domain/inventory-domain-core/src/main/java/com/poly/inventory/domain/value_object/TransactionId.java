package com.poly.inventory.domain.value_object;

import java.util.Objects;

public class TransactionId {
    private final Integer value;

    public TransactionId(Integer value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("TransactionId must be a positive integer.");
        }
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    // equals & hashCode để đảm bảo hoạt động tốt trong Set, Map, v.v.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionId)) return false;
        TransactionId that = (TransactionId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "TransactionId{" + value + '}';
    }
}

