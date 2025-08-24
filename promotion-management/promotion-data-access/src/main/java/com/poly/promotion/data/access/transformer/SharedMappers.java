package com.poly.promotion.data.access.transformer;

import com.poly.promotion.domain.core.valueobject.*;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * <h2>SharedMappers Class</h2>
 * 
 * <p>Shared mapper classes for common value object conversions used across multiple transformers.
 * This eliminates duplication and ensures consistency in mapping logic.</p>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 */
public class SharedMappers {

    /**
     * <h3>VoucherIdMapper Class</h3>
     * 
     * <p>Custom mapper for converting between VoucherId value objects and String primitives.</p>
     */
    @Mapper(componentModel = "spring")
    public abstract static class VoucherIdMapper {

        @Named("voucherIdToString")
        public String voucherIdToString(VoucherId voucherId) {
            return voucherId != null ? voucherId.getValue().toString() : null;
        }

        @Named("stringToVoucherId")
        public VoucherId stringToVoucherId(String id) {
            return id != null ? new VoucherId(UUID.fromString(id)) : null;
        }
    }

    /**
     * <h3>VoucherPackIdMapper Class</h3>
     * 
     * <p>Custom mapper for converting between VoucherPackId value objects and Long primitives.</p>
     */
    @Mapper(componentModel = "spring")
    public abstract static class VoucherPackIdMapper {

        @Named("voucherPackIdToLong")
        public Long voucherPackIdToLong(VoucherPackId voucherPackId) {
            return voucherPackId != null ? voucherPackId.getValue() : null;
        }

        @Named("longToVoucherPackId")
        public VoucherPackId longToVoucherPackId(Long id) {
            return id != null ? new VoucherPackId(id) : null;
        }
    }

    /**
     * <h3>CustomerIdMapper Class</h3>
     * 
     * <p>Custom mapper for converting between CustomerId value objects and String primitives.</p>
     */
    @Mapper(componentModel = "spring")
    public abstract static class CustomerIdMapper {

        @Named("customerIdToString")
        public String customerIdToString(CustomerId customerId) {
            return customerId != null ? customerId.getValue().toString() : null;
        }

        @Named("stringToCustomerId")
        public CustomerId stringToCustomerId(String id) {
            return id != null ? new CustomerId(UUID.fromString(id)) : null;
        }
    }

    /**
     * <h3>DiscountMapper Class</h3>
     * 
     * <p>Custom mapper for converting between Discount value objects and BigDecimal primitives.</p>
     */
    @Mapper(componentModel = "spring")
    public abstract static class DiscountMapper {

        @Named("discountToBigDecimal")
        public BigDecimal discountToBigDecimal(Discount discount) {
            return discount != null ? discount.getValue() : null;
        }

        @Named("bigDecimalToDiscount")
        public Discount bigDecimalToDiscount(BigDecimal amount) {
            if (amount == null) {
                return null;
            }
            
            // Default to percentage discount for values <= 100
            // This is a simplified approach - in practice, you might need more context
            if (amount.compareTo(BigDecimal.valueOf(100)) <= 0) {
                return new DiscountPercentage(amount.doubleValue());
            } else {
                // For fixed amount discounts, create DiscountAmount
                return new DiscountAmount(new Money(amount));
            }
        }
    }

    /**
     * <h3>DateRangeMapper Class</h3>
     * 
     * <p>Custom mapper for converting between DateRange value objects and String primitives.</p>
     */
    @Mapper(componentModel = "spring")
    public abstract static class DateRangeMapper {

        @Named("dateRangeToString")
        public String dateRangeToString(DateRange dateRange) {
            return dateRange != null ? dateRange.toString() : null;
        }

        @Named("stringToDateRange")
        public DateRange stringToDateRange(String dateRangeString) {
            if (dateRangeString == null || dateRangeString.trim().isEmpty()) {
                return null;
            }
            
            try {
                // Parse the date range string in format "X UNIT"
                String[] parts = dateRangeString.trim().split("\\s+");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid date range format. Expected 'X UNIT' format.");
                }
                
                long value = Long.parseLong(parts[0]);
                String unitStr = parts[1].toUpperCase();
                
                // Map string units to ChronoUnit
                java.time.temporal.ChronoUnit unit;
                switch (unitStr) {
                    case "DAYS":
                        unit = java.time.temporal.ChronoUnit.DAYS;
                        break;
                    case "WEEKS":
                        unit = java.time.temporal.ChronoUnit.WEEKS;
                        break;
                    case "MONTHS":
                        unit = java.time.temporal.ChronoUnit.MONTHS;
                        break;
                    case "YEARS":
                        unit = java.time.temporal.ChronoUnit.YEARS;
                        break;
                    case "HOURS":
                        unit = java.time.temporal.ChronoUnit.HOURS;
                        break;
                    case "MINUTES":
                        unit = java.time.temporal.ChronoUnit.MINUTES;
                        break;
                    case "SECONDS":
                        unit = java.time.temporal.ChronoUnit.SECONDS;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown time unit: " + unitStr);
                }
                
                return new DateRange((int) value, unit);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid number in date range: " + dateRangeString, e);
            }
        }
    }
}
