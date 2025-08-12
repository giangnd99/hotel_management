package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum VoucherStatus {
    PENDING("pending"),
    REDEEMED("redeemed"),
    EXPIRED("expired"),
    USED("used");

    String statusName;

    public static VoucherStatus fromString(String status) {
        for (VoucherStatus voucherStatus : values()) {
            if (voucherStatus.getStatusName().equalsIgnoreCase(status)) {
                return voucherStatus;
            }
        }
        throw new IllegalArgumentException("Unknown VoucherStatus: " + status);
    }

    public boolean canTransitionTo(VoucherStatus newStatus) {
        switch (this) {
            case PENDING:
                return newStatus == REDEEMED || newStatus == EXPIRED;
            case REDEEMED:
                return newStatus == USED || newStatus == EXPIRED;
            case EXPIRED:
            case USED:
                return false; // Terminal states
            default:
                return false;
        }
    }
}
