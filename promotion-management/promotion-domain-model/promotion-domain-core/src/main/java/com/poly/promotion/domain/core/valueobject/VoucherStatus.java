package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum VoucherStatus {
    PENDING(0, "pending"),
    REDEEMED(1, "redeemed"),
    EXPIRED(2, "expired"),
    USED(3, "used");

    Integer statusCode;
    String statusName;

    public static VoucherStatus fromStatusCode(Integer statusCode) {
        for (VoucherStatus status : values()) {
            if (status.getStatusCode().equals(statusCode)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid VoucherStatus code: " + statusCode);
    }
}
