package com.poly.paymentdomain.model.entity.value_object;

import lombok.Getter;

import java.util.UUID;

@Getter
public class VoucherId {
    private final UUID value;

    public VoucherId(UUID value) {
        this.value = value;
    }

    public static VoucherId system() {
        return new VoucherId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    public static VoucherId from(UUID value) {
        return new VoucherId(value);
    }

    public static UUID to(VoucherId value) {
        return UUID.fromString(value.getValue().toString());
    }

    public UUID getValue() {
        return value;
    }
}
