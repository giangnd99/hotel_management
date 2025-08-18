package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.temporal.ChronoUnit;

/**
 * <h2>DateRange Class</h2>
 * 
 * <p>Represents a duration or time span defined by a numeric value and a time unit.
 * This value object is used to specify validity periods for vouchers and other time-based
 * business rules in the promotion system.</p>
 * 
 * <p><strong>Usage Examples:</strong></p>
 * <ul>
 *   <li>Voucher validity: 30 days, 2 weeks, 6 months</li>
 *   <li>Promotion duration: 1 year, 3 months</li>
 *   <li>Booking window: 24 hours, 7 days</li>
 * </ul>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Value must be positive (greater than 0)</li>
 *   <li>Time unit cannot be null</li>
 *   <li>Only appropriate time units are allowed (no nanoseconds, microseconds, or milliseconds)</li>
 *   <li>Instances are immutable for thread safety</li>
 * </ul>
 * 
 * <p><strong>Supported Time Units:</strong></p>
 * <ul>
 *   <li><strong>Days:</strong> DAYS</li>
 *   <li><strong>Weeks:</strong> WEEKS</li>
 *   <li><strong>Months:</strong> MONTHS</li>
 *   <li><strong>Years:</strong> YEARS</li>
 *   <li><strong>Hours:</strong> HOURS</li>
 *   <li><strong>Minutes:</strong> MINUTES</li>
 *   <li><strong>Seconds:</strong> SECONDS</li>
 * </ul>
 * 
 * <p><strong>Immutability:</strong></p>
 * <p>This class is immutable, meaning once created, its values cannot be changed.
 * This ensures thread safety and prevents accidental modifications.</p>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see ChronoUnit
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DateRange {
    
    /**
     * The numeric value representing the duration.
     * Must be positive and represents the quantity of the specified time unit.
     * 
     * <p>Examples: 30 (for 30 days), 2 (for 2 weeks), 6 (for 6 months)</p>
     */
    private final int value;
    
    /**
     * The time unit for the duration.
     * Defines the granularity of the time span (e.g., DAYS, WEEKS, MONTHS).
     * 
     * <p>This field is final to ensure immutability.</p>
     */
    private final ChronoUnit unit;

    /**
     * Creates a new DateRange with the specified value and time unit.
     * 
     * <p>This constructor performs validation to ensure the DateRange is valid:</p>
     * <ul>
     *   <li>Value must be positive</li>
     *   <li>Unit cannot be null</li>
     *   <li>Unit must be an appropriate time unit for business operations</li>
     * </ul>
     * 
     * @param value the numeric value of the duration (must be positive)
     * @param unit the time unit for the duration (cannot be null)
     * @throws IllegalArgumentException if value is not positive, unit is null, or unit is inappropriate
     */
    public DateRange(int value, ChronoUnit unit) {
        if (value <= 0) {
            throw new IllegalArgumentException("Date range value must be positive");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Date range unit cannot be null");
        }
        if (unit == ChronoUnit.NANOS || unit == ChronoUnit.MICROS || unit == ChronoUnit.MILLIS) {
            throw new IllegalArgumentException("Invalid time unit for voucher validity: " + unit);
        }
        this.value = value;
        this.unit = unit;
    }

    /**
     * Returns a human-readable string representation of this DateRange.
     * 
     * <p>The format is "value unit" where the unit is converted to lowercase for readability.
     * Examples: "30 days", "2 weeks", "6 months"</p>
     * 
     * @return a formatted string representation of the date range
     */
    @Override
    public String toString() {
        return value + " " + unit.name().toLowerCase();
    }

    /**
     * Calculates the total duration in a specific time unit.
     * 
     * <p>This utility method converts the DateRange to a different time unit for
     * comparison or calculation purposes.</p>
     * 
     * @param targetUnit the target time unit for conversion
     * @return the duration value converted to the target unit
     * @throws IllegalArgumentException if targetUnit is null
     */
    public long toUnit(ChronoUnit targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        
        // Simple conversion logic for common time units
        if (targetUnit == ChronoUnit.DAYS) {
            switch (unit) {
                case DAYS: return value;
                case WEEKS: return value * 7;
                case MONTHS: return value * 30;
                case YEARS: return value * 365;
                case HOURS: return value / 24;
                case MINUTES: return value / (24 * 60);
                case SECONDS: return value / (24 * 60 * 60);
                default: return value;
            }
        }
        
        // For other units, return the original value for now
        // In a production environment, you might want more sophisticated conversion logic
        return value;
    }

    /**
     * Checks if this DateRange represents a longer duration than another DateRange.
     * 
     * <p>This method converts both DateRanges to a common unit (days) for comparison.</p>
     * 
     * @param other the DateRange to compare with
     * @return true if this DateRange is longer than the other, false otherwise
     * @throws IllegalArgumentException if other is null
     */
    public boolean isLongerThan(DateRange other) {
        if (other == null) {
            throw new IllegalArgumentException("Other DateRange cannot be null");
        }
        
        long thisInDays = toUnit(ChronoUnit.DAYS);
        long otherInDays = other.toUnit(ChronoUnit.DAYS);
        
        return thisInDays > otherInDays;
    }

    /**
     * Checks if this DateRange represents a shorter duration than another DateRange.
     * 
     * <p>This method converts both DateRanges to a common unit (days) for comparison.</p>
     * 
     * @param other the DateRange to compare with
     * @return true if this DateRange is shorter than the other, false otherwise
     * @throws IllegalArgumentException if other is null
     */
    public boolean isShorterThan(DateRange other) {
        if (other == null) {
            throw new IllegalArgumentException("Other DateRange cannot be null");
        }
        
        long thisInDays = toUnit(ChronoUnit.DAYS);
        long otherInDays = other.toUnit(ChronoUnit.DAYS);
        
        return thisInDays < otherInDays;
    }
}
