package com.poly.promotion.domain.core.valueobject;

public class DiscountPercentage implements Discount {
    private final double percentage;

    public DiscountPercentage(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = percentage;
    }

    @Override
    public Double getValue() {
        return percentage;
    }

    @Override
    public String toString() {
        return percentage + "%";
    }
}
