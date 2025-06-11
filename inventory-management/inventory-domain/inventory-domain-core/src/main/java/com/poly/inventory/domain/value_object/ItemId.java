package com.poly.inventory.domain.value_object;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemId implements Serializable {

    @Column(name = "item_id")
    private Integer value;

    public ItemId() {

    }

    private ItemId(Integer value) {
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
