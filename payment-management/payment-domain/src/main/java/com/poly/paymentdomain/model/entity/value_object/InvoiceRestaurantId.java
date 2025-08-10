package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoiceRestaurantId extends BaseId<UUID> {
    protected InvoiceRestaurantId(UUID value) {
        super(value);
    }
}
