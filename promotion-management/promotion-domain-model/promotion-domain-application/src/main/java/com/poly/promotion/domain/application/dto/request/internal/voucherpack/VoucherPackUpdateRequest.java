package com.poly.promotion.domain.application.dto.request.internal.voucherpack;

import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.DiscountAmount;
import com.poly.promotion.domain.core.valueobject.DiscountPercentage;
import com.poly.domain.valueobject.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPackUpdateRequest {
    String description;
    BigDecimal discountAmount;
    String voucherValidRange;
    Long requiredPoints;
    Integer quantity;
    LocalDate packValidFrom;
    LocalDate packValidTo;

    public VoucherPack toEntity(){
        // Create appropriate discount based on amount
        Discount discount = null;
        if (discountAmount != null) {
            if (discountAmount.compareTo(BigDecimal.valueOf(100)) <= 0) {
                discount = new DiscountPercentage(discountAmount.doubleValue());
            } else {
                discount = new DiscountAmount(new Money(discountAmount));
            }
        }
        
        // Parse voucher valid range
        DateRange validRange = null;
        if (voucherValidRange != null && !voucherValidRange.isEmpty()) {
            validRange = parseVoucherValidRange(voucherValidRange);
        }
        
        return VoucherPack.builder()
                .description(description)
                .discountAmount(discount)
                .voucherValidRange(validRange)
                .requiredPoints(requiredPoints)
                .quantity(quantity)
                .packValidFrom(packValidFrom)
                .packValidTo(packValidTo)
                .build();
    }

    private DateRange parseVoucherValidRange(String voucherValidRange) {
        if (voucherValidRange == null || voucherValidRange.isEmpty()) {
            throw new IllegalArgumentException("Voucher valid range cannot be null or empty");
        }
        
        String[] parts = voucherValidRange.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid date range format. Expected format: '<value> <unit>'");
        }
        
        try {
            int value = Integer.parseInt(parts[0]);
            ChronoUnit unit = ChronoUnit.valueOf(parts[1].toUpperCase());
            return new DateRange(value, unit);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date range format. Expected format: '<value: int> <unit: ChronoUnit string>'", e);
        }
    }
}
