package com.poly.payment.management.domain.value_object;

import lombok.Getter;

import java.util.UUID;

@Getter
public class StaffId {
    private final UUID value;

    public StaffId(UUID value) {
        this.value = value;
    }

    public static StaffId from(UUID value) {
        return new StaffId(value);
    }

    public static UUID to(StaffId staffId) {
        return UUID.fromString(staffId.value.toString());
    }

    public static StaffId system() {
        return new StaffId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    public UUID getValue() {
        return value;
    }
}
