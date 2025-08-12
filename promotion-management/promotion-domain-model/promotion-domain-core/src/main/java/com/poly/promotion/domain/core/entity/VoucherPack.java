package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.promotion.domain.core.exception.PromotionDomainException;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPack extends BaseEntity<VoucherPackId> {
    String description;
    Discount discountAmount;
    DateRange voucherValidRange;
    Long requiredPoints;
    Integer quantity;
    LocalDate packValidFrom;
    LocalDate packValidTo;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime updatedAt;
    String updatedBy;
    VoucherPackStatus status;

    // Domain validation methods
    public void validatePackDates() {
        if (packValidFrom != null && packValidTo != null && packValidFrom.isAfter(packValidTo)) {
            throw new PromotionDomainException("Pack valid from date must be before valid to date");
        }
    }

    public void validateQuantity() {
        if (quantity == null || quantity <= 0) {
            throw new PromotionDomainException("Quantity must be positive");
        }
    }

    public void validateRequiredPoints() {
        if (requiredPoints == null || requiredPoints <= 0) {
            throw new PromotionDomainException("Required points must be positive");
        }
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return status == VoucherPackStatus.PUBLISHED &&
               (packValidFrom == null || !today.isBefore(packValidFrom)) &&
               (packValidTo == null || !today.isAfter(packValidTo)) &&
               quantity > 0;
    }

    public boolean canRedeem(Integer requestedQuantity) {
        return isActive() && requestedQuantity > 0 && requestedQuantity <= quantity;
    }

    public void redeem(Integer quantity) {
        if (!canRedeem(quantity)) {
            throw new PromotionDomainException("Cannot redeem " + quantity + " vouchers from pack " + getId());
        }
        this.quantity -= quantity;
        if (this.quantity <= 0) {
            this.status = VoucherPackStatus.CLOSED;
        }
    }
}
