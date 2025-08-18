package com.poly.payment.management.domain.value_object;

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

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID());
    }
}
