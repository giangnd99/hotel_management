package com.poly.paymentdomain.model.entity.value_object;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CustomerId {
    private final UUID value;

    public CustomerId(UUID value) {
        this.value = value;
    }

    public static CustomerId fromValue(UUID value) {
        return new CustomerId(value);
    }

    public static UUID to(CustomerId customerId) {
        return UUID.fromString(customerId.value.toString());
    }

    public UUID getValue() {
        return value;
    }
}
