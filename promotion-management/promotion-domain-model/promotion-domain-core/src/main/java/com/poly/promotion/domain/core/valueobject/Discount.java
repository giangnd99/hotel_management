package com.poly.promotion.domain.core.valueobject;

import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;

public interface Discount {
    BigDecimal getValue();
    Money calculateDiscountAmount(Money originalPrice);
    String getDisplayValue();
}
