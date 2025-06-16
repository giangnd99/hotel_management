package com.poly.inventory.domain.value_object;

import java.util.Objects;

public class ItemId {

    private Integer value;

    public ItemId() {

    }

    public ItemId(Integer value) {
        this.value = value;
    }

    public static ItemId from(Integer value) {
        return new ItemId(value);
    }

    public Integer getValue() {
        return value;
    }

    public static ItemId of(Integer value) {
        return new ItemId(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemId itemId)) return false;
        return Objects.equals(value, itemId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
