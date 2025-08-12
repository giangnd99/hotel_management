package com.poly.promotion.domain.core.event;

import com.poly.domain.valueobject.CustomerId;
import com.poly.promotion.domain.core.valueobject.VoucherId;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;

public class VoucherRedeemedEvent extends DomainEvent {
    private final VoucherId voucherId;
    private final CustomerId customerId;
    private final VoucherPackId voucherPackId;
    private final Long requiredPoints;

    public VoucherRedeemedEvent(VoucherId voucherId, CustomerId customerId, VoucherPackId voucherPackId, Long requiredPoints) {
        super("VoucherRedeemed");
        this.voucherId = voucherId;
        this.customerId = customerId;
        this.voucherPackId = voucherPackId;
        this.requiredPoints = requiredPoints;
    }

    public VoucherId getVoucherId() {
        return voucherId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public VoucherPackId getVoucherPackId() {
        return voucherPackId;
    }

    public Long getRequiredPoints() {
        return requiredPoints;
    }
}
