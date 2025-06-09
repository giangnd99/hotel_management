package com.poly.customerdomain.model.valueobject;

public record Address(String value) {

    public Address {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Địa chỉ không được để trống !!!");
        }
    }

}