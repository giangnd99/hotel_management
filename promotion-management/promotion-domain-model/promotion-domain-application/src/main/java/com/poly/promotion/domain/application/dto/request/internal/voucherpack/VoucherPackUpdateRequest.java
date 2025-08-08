package com.poly.promotion.domain.application.dto.request.internal.voucherpack;

import com.poly.promotion.domain.core.entity.VoucherPack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPackUpdateRequest {
    String description;
    Double discountAmount;
    String voucherValidRange;
    Long requiredPoints;
    Integer quantity;
    LocalDate packValidFrom;
    LocalDate packValidTo;

    public VoucherPack toEntity(){
        return VoucherPack.builder()
                .description(description)
                .discountAmount(discountAmount)
                .validRange(voucherValidRange)
                .requiredPoints(requiredPoints)
                .quantity(quantity)
                .validFrom(packValidFrom)
                .validTo(packValidTo)
                .build();
    }
}
