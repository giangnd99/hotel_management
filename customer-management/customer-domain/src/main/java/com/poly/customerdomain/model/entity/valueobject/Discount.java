package com.poly.customerdomain.model.entity.valueobject;

import java.math.BigDecimal;

public class Discount {

    private final BigDecimal percentage; // optional
    private final BigDecimal amount;     // optional

    public Discount(BigDecimal percentage, BigDecimal amount) {
        if (percentage != null) {
            if (percentage.compareTo(BigDecimal.ZERO) < 0 || percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("Discount percentage must be 0–100");
            }
        }
        if (amount != null && amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount amount must be >= 0");
        }
        this.percentage = percentage;
        this.amount = amount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public static Discount zeroDiscount(){
        return new Discount(BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
