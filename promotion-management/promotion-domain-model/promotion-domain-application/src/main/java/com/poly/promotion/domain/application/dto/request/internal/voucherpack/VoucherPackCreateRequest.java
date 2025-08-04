package com.poly.promotion.domain.application.dto.request.internal.voucherpack;

import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.exception.PromotionDomainException;
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
public class VoucherPackCreateRequest {
    String description;
    Double discountAmount;
    String voucherValidRange;
    Long requiredPoints;
    Integer quantity;
    LocalDate packValidFrom;
    LocalDate packValidTo;

    public void validateRequest(){
        if (description == null || description.isEmpty()) {
            throw new PromotionDomainException("Description cannot be null or empty");
        }
        if (discountAmount == null || discountAmount <= 0) {
            throw new PromotionDomainException("Discount amount must be greater than zero");
        }
        if (voucherValidRange == null || voucherValidRange.isEmpty() ) {
            throw new PromotionDomainException("Voucher valid range cannot be null or empty");
        }
        if (requiredPoints == null || requiredPoints <= 0) {
            throw new PromotionDomainException("Required points must be greater than zero");
        }
        if (quantity == null || quantity <= 0) {
            throw new PromotionDomainException("Quantity must be greater than zero");
        }
        if (packValidFrom == null || packValidTo == null || packValidFrom.isAfter(packValidTo) || packValidFrom.isBefore(LocalDate.now())) {
            throw new PromotionDomainException("Pack valid range is invalid");
        }
    }

    public VoucherPack toEntity() {
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
