package com.poly.promotion.domain.application.dto.response.external;

import com.poly.promotion.domain.core.entity.VoucherPack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPackExternalResponse {
    String description;
    Double discountAmount;
    String validRange;
    Long requiredPoints;
    Integer quantity;
    LocalDate validFrom;
    LocalDate validTo;
    String packStatus;

    public static VoucherPackExternalResponse fromEntity(VoucherPack voucherPack) {
        return VoucherPackExternalResponse.builder()
                .description(voucherPack.getDescription())
                .discountAmount(voucherPack.getDiscountAmount().getValue())
                .validRange(voucherPack.getVoucherValidRange().toString())
                .requiredPoints(voucherPack.getRequiredPoints())
                .quantity(voucherPack.getQuantity())
                .validFrom(voucherPack.getPackValidFrom())
                .validTo(voucherPack.getPackValidTo())
                .packStatus(voucherPack.getStatus().name())
                .build();
    }
}
