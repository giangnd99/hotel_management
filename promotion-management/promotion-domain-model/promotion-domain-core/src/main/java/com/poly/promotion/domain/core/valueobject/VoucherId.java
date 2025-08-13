package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

/**
 * <h2>VoucherId Value Object</h2>
 * 
 * <p>Represents the unique identifier for a voucher. This value object is used to
 * uniquely identify each voucher in the system. It is immutable and thread-safe,
 * ensuring consistent behavior across different parts of the application.</p>
 * 
 */
public class VoucherId extends BaseId<UUID> {
    public VoucherId(UUID value) {
        super(value);
    }
}
