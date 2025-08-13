package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoiceRestaurantId extends BaseId<UUID> {
    protected InvoiceRestaurantId(UUID value) {
        super(value);
    }

    public static InvoiceRestaurantId from(UUID value) {
        return new InvoiceRestaurantId(value);
    }

    public static InvoiceRestaurantId generate() {
        return new InvoiceRestaurantId(UUID.randomUUID());
    }
}
