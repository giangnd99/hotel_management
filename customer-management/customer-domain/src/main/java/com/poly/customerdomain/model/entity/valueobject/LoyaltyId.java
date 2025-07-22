package com.poly.customerdomain.model.entity.valueobject;

import java.util.UUID;

public class LoyaltyId {

    private final UUID value;

    public LoyaltyId(UUID value) {
        this.value = value;
    }

    public static LoyaltyId generate() {
        return new LoyaltyId(UUID.randomUUID());
    }

    public static UUID toUUID(LoyaltyId loyaltyId) {
        return UUID.fromString(loyaltyId.value.toString());
    }

    public static LoyaltyId fromUUID(UUID value) {
        return new LoyaltyId(value);
    }

    public UUID getValue() {
        return value;
    }
}
