package com.poly.paymentdomain.model.entity.value_object;

import com.poly.paymentdomain.model.exception.InvalidValueException;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Money {
    private final BigDecimal value;

    public Money(BigDecimal value) {
        if (value == null) throw new InvalidValueException("Money");
        this.value = value;
    }

    public Money multiply(BigDecimal multiplicand) {
        return  new Money(this.getValue().multiply(multiplicand));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

//    public static Money add(Money money1, Money money2) {
//        return new Money(money1.getValue().add(money2.getValue()));
//    }

    public Money add(Money other) {
        return new Money(this.value.add(other.value));
    }

    public Money subtract(Money other) {
        return new Money(this.value.subtract(other.value));
    }

    public static Money from(BigDecimal value) {
        return new Money(value);
    }

    public static BigDecimal to(Money money) {
        return BigDecimal.valueOf(money.value.doubleValue());
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isGreaterThan(Money moneyCanCheck) {
        return this.getValue().compareTo(moneyCanCheck.getValue()) > 0;
    }

    public boolean isGreaterThanOrEqualTo(Money moneyCanCheck) {
        return this.getValue().compareTo(moneyCanCheck.getValue()) >= 0;
    }
}
