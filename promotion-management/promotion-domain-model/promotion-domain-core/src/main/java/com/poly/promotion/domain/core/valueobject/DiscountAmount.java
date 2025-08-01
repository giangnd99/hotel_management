package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;

public class DiscountAmount implements Discount{
    private final Money value;

    public DiscountAmount(Money value) {
        if(!value.isGreaterThan(new Money(BigDecimal.valueOf(999.99)))) {
            throw new IllegalArgumentException("Discount amount must be greater than or equal 1000");
        }
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value.getAmount().doubleValue();
    }
}
