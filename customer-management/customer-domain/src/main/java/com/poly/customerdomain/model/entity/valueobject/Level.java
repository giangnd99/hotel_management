package com.poly.customerdomain.model.entity.valueobject;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum Level {
    NONE(BigDecimal.ZERO),
    BRONZE(BigDecimal.valueOf(5_000)),
    SILVER(BigDecimal.valueOf(10_000)),
    GOLD(BigDecimal.valueOf(30_000)),
    PLATINUM(BigDecimal.valueOf(50_000)),
    DIAMOND(BigDecimal.valueOf(100_000));

    private final BigDecimal spendingRequired;

    Level(BigDecimal spendingRequired) {
        this.spendingRequired = spendingRequired;
    }

    public Level next() {
        return switch (this) {
            case NONE -> BRONZE;
            case BRONZE -> SILVER;
            case SILVER -> GOLD;
            case GOLD -> PLATINUM;
            case PLATINUM -> DIAMOND;
            case DIAMOND -> throw new IllegalStateException("Already at highest level");
        };
    }

    public boolean isMaxLevel() {
        return this == DIAMOND;
    }
}