package com.poly.customerdomain.model.entity.valueobject;

import java.math.BigDecimal;

public class Point {

    private final BigDecimal value;

    public Point(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Point must be >= 0");
        }
        this.value = value;
    }

    public static Point from(BigDecimal value) {
        return new Point(value);
    }

    public static Point zero() {
        return new Point(BigDecimal.ZERO);
    }

    public Point add(Point other) {
        return new Point(this.value.add(other.value));
    }

    public Point subtract(Point other) {
        BigDecimal result = this.value.subtract(other.value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cannot subtract more points than available");
        }
        return new Point(result);
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isLessThan(Point other) {
        return this.value.compareTo(other.value) < 0;
    }

}
