package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class VoucherId extends BaseId<UUID> {
    public VoucherId(UUID value) {
        super(value);
    }
}
