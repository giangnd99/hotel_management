package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;

public class DiscountAmount implements Discount {
    private final Money value;

    public DiscountAmount(Money value) {
        if (!value.isGreaterThan(new Money(BigDecimal.valueOf(999.99)))) {
            throw new IllegalArgumentException("Discount amount must be greater than or equal 1000 VND");
        }
        this.value = value;
    }

    @Override
    public BigDecimal getValue() {
        return value.getAmount();
    }

    @Override
    public Money calculateDiscountAmount(Money originalPrice) {
        // For fixed amount discount, return the discount amount directly
        // but ensure it doesn't exceed the original price
        if (value.isGreaterThan(originalPrice)) {
            return originalPrice;
        }
        return value;
    }

    @Override
    public String getDisplayValue() {
        return value.getAmount().toString() + " VND";
    }

    @Override
    public String toString() {
        return getDisplayValue();
    }
}
