package com.poly.restaurant.domain.value_object;

import java.util.Objects;

public class MenuItemId {
    private final String value;

    public MenuItemId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("MenuItemId value cannot be null or empty");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItemId)) return false;
        MenuItemId that = (MenuItemId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

