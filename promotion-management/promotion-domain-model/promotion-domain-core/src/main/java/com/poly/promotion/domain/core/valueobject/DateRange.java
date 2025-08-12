package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.temporal.ChronoUnit;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DateRange {
    int value;
    ChronoUnit unit;

    public DateRange(int value, ChronoUnit unit) {
        if (value <= 0) {
            throw new IllegalArgumentException("Date range value must be positive");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Date range unit cannot be null");
        }
        // Validate that the unit is appropriate for voucher validity
        if (unit == ChronoUnit.NANOS || unit == ChronoUnit.MICROS || unit == ChronoUnit.MILLIS) {
            throw new IllegalArgumentException("Invalid time unit for voucher validity: " + unit);
        }
        this.value = value;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return value + " " + unit.name().toLowerCase();
    }
}
