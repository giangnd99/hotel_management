package com.poly.customerdomain.model.entity.valueobject;

import com.poly.customerdomain.model.exception.InvalidCustomerUserIdException;

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
