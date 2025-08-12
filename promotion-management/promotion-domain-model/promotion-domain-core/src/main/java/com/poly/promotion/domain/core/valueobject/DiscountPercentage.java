package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountPercentage implements Discount {
    private final BigDecimal percentage;

    public DiscountPercentage(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = BigDecimal.valueOf(percentage);
    }

    @Override
    public BigDecimal getValue() {
        return percentage;
    }

    @Override
    public Money calculateDiscountAmount(Money originalPrice) {
        BigDecimal discountAmount = originalPrice.getAmount()
                .multiply(percentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return new Money(discountAmount);
    }

    @Override
    public String getDisplayValue() {
        return percentage + "%";
    }

    @Override
    public String toString() {
        return getDisplayValue();
    }
}
