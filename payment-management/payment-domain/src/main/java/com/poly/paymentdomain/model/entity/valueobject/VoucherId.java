package com.poly.paymentdomain.model.entity.valueobject;

import com.poly.paymentdomain.model.exception.InvalidValueException;
import lombok.Getter;

import java.util.UUID;

@Getter
public class VoucherId {
    private final UUID value;

    public VoucherId(UUID value) {
        if (value == null) throw new InvalidValueException("VoucherId");
        this.value = value;
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
