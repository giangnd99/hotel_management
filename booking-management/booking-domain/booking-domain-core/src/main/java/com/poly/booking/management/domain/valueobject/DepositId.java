package com.poly.booking.management.domain.valueobject;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class DepositId extends BaseId<UUID> {
    public DepositId(UUID value) {
        super(value);
    }
}
