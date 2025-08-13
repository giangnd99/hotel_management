package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.BaseId;

/**
 * <h2>VoucherPackId Value Object</h2>
 * 
 * <p>Represents the unique identifier for a voucher pack. This value object is used to
 * uniquely identify each voucher pack in the system. It is immutable and thread-safe,
 * ensuring consistent behavior across different parts of the application.</p>
 */
public class VoucherPackId extends BaseId<Long> {
    public VoucherPackId(Long value) {
        super(value);
    }
}
