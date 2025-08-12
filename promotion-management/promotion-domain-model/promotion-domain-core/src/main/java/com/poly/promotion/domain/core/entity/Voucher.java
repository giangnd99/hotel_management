package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.CustomerId;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.VoucherId;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;
import com.poly.promotion.domain.core.exception.PromotionDomainException;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher extends BaseEntity<VoucherId> {
    CustomerId customerId;
    VoucherPackId voucherPackId;
    String voucherCode;
    Discount discount;
    LocalDateTime redeemedAt;
    LocalDateTime validTo;
    VoucherStatus voucherStatus;

    public static Voucher initRedeem(String customerId, Long voucherPackId, Discount discount, DateRange voucherValidRange) {
        return Voucher.builder()
                .customerId(new CustomerId(UUID.fromString(customerId)))
                .voucherPackId(new VoucherPackId(voucherPackId))
                .voucherCode(UUID.randomUUID().toString()) // Should use a more proper voucher code generation strategy
                .discount(discount)
                .redeemedAt(LocalDateTime.now())
                .validTo(LocalDateTime.now().plus(voucherValidRange.getValue(), voucherValidRange.getUnit()))
                .voucherStatus(VoucherStatus.PENDING)
                .build();
    }

    // Domain validation methods
    public boolean isValid() {
        return voucherStatus == VoucherStatus.REDEEMED && 
               LocalDateTime.now().isBefore(validTo);
    }

    public boolean canUse() {
        return voucherStatus == VoucherStatus.REDEEMED && isValid();
    }

    public void use() {
        if (!canUse()) {
            throw new PromotionDomainException("Voucher cannot be used. Status: " + voucherStatus + ", Valid: " + isValid());
        }
        this.voucherStatus = VoucherStatus.USED;
    }

    public void expire() {
        if (voucherStatus == VoucherStatus.REDEEMED && LocalDateTime.now().isAfter(validTo)) {
            this.voucherStatus = VoucherStatus.EXPIRED;
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(validTo);
    }
}
