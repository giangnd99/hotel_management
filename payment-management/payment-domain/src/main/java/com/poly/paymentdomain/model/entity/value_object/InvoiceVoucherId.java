package com.poly.paymentdomain.model.entity.value_object;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class InvoiceVoucherId extends BaseId<UUID> {
    protected InvoiceVoucherId(UUID value) {
        super(value);
    }
}
