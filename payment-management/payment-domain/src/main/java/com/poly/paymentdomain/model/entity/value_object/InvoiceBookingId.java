package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoiceBookingId extends BaseId<UUID> {
    protected InvoiceBookingId(UUID value) {
        super(value);
    }
}
