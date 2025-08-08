package com.poly.promotion.domain.application.dto.response.internal;

import com.poly.promotion.domain.core.entity.VoucherPack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPackInternalResponse {
    Long id;
    String description;
    Double discountAmount;
    String validRange;
    Long requiredPoints;
    Integer quantity;
    LocalDate validFrom;
    LocalDate validTo;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime updatedAt;
    String updatedBy;
    String status;

    public static VoucherPackInternalResponse fromEntity(VoucherPack voucherPack) {
        return VoucherPackInternalResponse.builder()
                .id(voucherPack.getId().getValue())
                .description(voucherPack.getDescription())
                .discountAmount(voucherPack.getDiscountAmount().getValue())
                .validRange(voucherPack.getVoucherValidRange().toString())
                .requiredPoints(voucherPack.getRequiredPoints())
                .quantity(voucherPack.getQuantity())
                .validFrom(voucherPack.getPackValidFrom())
                .validTo(voucherPack.getPackValidTo())
                .createdAt(voucherPack.getCreatedAt())
                .createdBy(voucherPack.getCreatedBy())
                .updatedAt(voucherPack.getUpdatedAt())
                .updatedBy(voucherPack.getUpdatedBy())
                .status(voucherPack.getStatus().name())
                .build();
    }
}
