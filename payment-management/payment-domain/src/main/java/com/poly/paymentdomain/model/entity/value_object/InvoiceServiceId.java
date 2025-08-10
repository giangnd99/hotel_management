package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoiceServiceId extends BaseId<UUID> {
    protected InvoiceServiceId(UUID value) {
        super(value);
    }
}
