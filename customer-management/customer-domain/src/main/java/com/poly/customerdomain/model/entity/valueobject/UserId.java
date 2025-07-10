package com.poly.customerdomain.model.entity.valueobject;

import java.util.UUID;

public class UserId {

    private final UUID value;

    public UserId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    public static UserId from(UUID value) {
        return new UserId(value);
    }

}
