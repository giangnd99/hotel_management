package com.poly.customerdomain.model.entity.valueobject;

import java.util.UUID;

public class TransactionId {

    private final UUID value;

    public TransactionId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    public static TransactionId from(UUID value) {
        return new TransactionId(value);
    }

    public static UUID from(TransactionId value) {
        return value.getValue();
    }
}
