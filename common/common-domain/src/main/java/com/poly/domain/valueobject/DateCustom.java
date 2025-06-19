package com.poly.domain.valueobject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateCustom {
    private final LocalDateTime value;

    private static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");

    private DateCustom(LocalDateTime value) {
        this.value = value;
    }

    public static DateCustom now() {
        return new DateCustom(LocalDateTime.now());
    }

    public static DateCustom of(LocalDateTime dateTime) {
        return new DateCustom(Objects.requireNonNull(dateTime, "DateTime must not be null"));
    }

    public static DateCustom of(int year, int month, int day, int hour, int minute, int second) {
        return new DateCustom(LocalDateTime.of(year, month, day, hour, minute, second));
    }

    public static DateCustom fromISOString(String isoString) {
        String normalizedString = isoString.replace("Z", "");
        LocalDateTime dateTime = LocalDateTime.parse(normalizedString,
                DateTimeFormatter.ofPattern(ISO_PATTERN.replace("'Z'", "")));
        return new DateCustom(dateTime);
    }

    public static DateCustom fromTimestamp(long timestamp) {
        return new DateCustom(LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                UTC_ZONE
        ));
    }

    public static DateCustom parse(String dateStr, String pattern) {
        return new DateCustom(LocalDateTime.parse(dateStr,
                DateTimeFormatter.ofPattern(pattern)));
    }

    public String toISOString() {
        return format(ISO_PATTERN);
    }

    public String format(String pattern) {
        return value.format(DateTimeFormatter.ofPattern(pattern));
    }

    public String format() {
        return format(DEFAULT_PATTERN);
    }

    public long toTimestamp() {
        return value.atZone(UTC_ZONE)
                .toInstant()
                .toEpochMilli();
    }

    public DateCustom atZone(String zoneId) {
        return new DateCustom(value.atZone(ZoneId.of(zoneId))
                .toLocalDateTime());
    }

    public DateCustom toUTC() {
        return atZone(UTC_ZONE.getId());
    }

    public boolean isBefore(DateCustom other) {
        return value.isBefore(other.value);
    }

    public boolean isAfter(DateCustom other) {
        return value.isAfter(other.value);
    }

    public boolean isBetween(DateCustom start, DateCustom end) {
        return !isBefore(start) && !isAfter(end);
    }

    public DateCustom plusDays(long days) {
        return new DateCustom(value.plusDays(days));
    }

    public DateCustom minusDays(long days) {
        return new DateCustom(value.minusDays(days));
    }

    public DateCustom plusHours(long hours) {
        return new DateCustom(value.plusHours(hours));
    }

    public DateCustom minusHours(long hours) {
        return new DateCustom(value.minusHours(hours));
    }

    public int getYear() {
        return value.getYear();
    }

    public int getMonth() {
        return value.getMonthValue();
    }

    public int getDay() {
        return value.getDayOfMonth();
    }

    public int getHour() {
        return value.getHour();
    }

    public int getMinute() {
        return value.getMinute();
    }

    public int getSecond() {
        return value.getSecond();
    }

    public LocalDateTime getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateCustom that = (DateCustom) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return format();
    }

}
