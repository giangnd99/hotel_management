package com.poly.customerdomain.model.entity.valueobject;

import java.util.UUID;

public class VoucherId {
    private final UUID value;

    public VoucherId(UUID value) {
        this.value = value;
    }

    public static VoucherId generate() {
        return new VoucherId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    // equals, hashCode
}
