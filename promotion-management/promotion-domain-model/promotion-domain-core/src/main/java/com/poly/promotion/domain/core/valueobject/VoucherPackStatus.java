package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum VoucherPackStatus {
    PENDING,
    PUBLISHED,
    CLOSED,
    EXPIRED;
    public static VoucherPackStatus fromString(String status) {
        for (VoucherPackStatus voucherPackStatus : VoucherPackStatus.values()) {
            if (voucherPackStatus.name().equalsIgnoreCase(status)) {
                return voucherPackStatus;
            }
        }
        throw new IllegalArgumentException("Unknown VoucherPackStatus: " + status);
    }
}
