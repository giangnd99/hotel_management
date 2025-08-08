package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DateRange {
    int value;
    ChronoUnit unit;

    @Override
    public String toString() {
        return value + " " + unit.name().toLowerCase();
    }
}
