package com.poly.promotion.domain.core.event;

import com.poly.promotion.domain.core.valueobject.VoucherPackId;

public class VoucherPackCreatedEvent extends DomainEvent {
    private final VoucherPackId voucherPackId;
    private final String description;
    private final Long requiredPoints;

    public VoucherPackCreatedEvent(VoucherPackId voucherPackId, String description, Long requiredPoints) {
        super("VoucherPackCreated");
        this.voucherPackId = voucherPackId;
        this.description = description;
        this.requiredPoints = requiredPoints;
    }

    public VoucherPackId getVoucherPackId() {
        return voucherPackId;
    }

    public String getDescription() {
        return description;
    }

    public Long getRequiredPoints() {
        return requiredPoints;
    }
}
