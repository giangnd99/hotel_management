package com.poly.promotion.domain.core.valueobject;

public enum PromotionStatus {
    PENDING(0),
    ACTIVE(1),
    EXPIRED(2),
    CLOSED(3);

    private final int code;

    PromotionStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PromotionStatus fromCode(int code) {
        for (PromotionStatus status : PromotionStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid PromotionStatus code: " + code);
    }
}
