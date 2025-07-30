package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum VoucherPackStatus {
    PENDING(0, "pending"),
    PUBLISHED(1, "published"),
    CLOSED(2, "closed"),
    EXPIRED(3, "expired");

    Integer statusCode;
    String statusName;

    public static VoucherPackStatus fromStatusCode(Integer statusCode) {
        for (VoucherPackStatus status : values()) {
            if (status.getStatusCode().equals(statusCode)) {
                return status;
            }
        }
        return null;
    }
}
