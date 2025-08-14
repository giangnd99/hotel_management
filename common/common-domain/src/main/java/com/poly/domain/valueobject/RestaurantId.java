package com.poly.domain.valueobject;

import java.util.UUID;

public class RestaurantId extends BaseId<UUID> {
    public RestaurantId(UUID value) {
        super(value);
    }

    public static RestaurantId from(UUID value) {
        return new RestaurantId(value);
    }
}
