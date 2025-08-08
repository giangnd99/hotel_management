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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher extends BaseEntity<VoucherId> {
    CustomerId customerId;
    VoucherPackId voucherPackId;
    String voucherCode;
    Discount discount;
    LocalDateTime redeemedAt;
    LocalDateTime validTo;
    VoucherStatus voucherStatus;

    private Voucher(Builder builder) {
        super.setId(builder.id);
        setCustomerId(builder.customerId);
        setVoucherPackId(builder.voucherPackId);
        setVoucherCode(builder.voucherCode);
        setDiscount(builder.discount);
        setRedeemedAt(builder.redeemedAt);
        setValidTo(builder.validTo);
        setVoucherStatus(builder.voucherStatus);
    }

    public static Voucher initRedeem(String customerId, Long voucherPackId, Discount discount, DateRange voucherValidRange) {
        return Voucher.builder()
                .customerId(customerId)
                .voucherPackId(voucherPackId)
                .voucherCode(UUID.randomUUID().toString()) // Should use a more proper voucher code generation strategy
                .discount(discount)
                .redeemedAt(LocalDateTime.now())
                .validTo(LocalDateTime.now().plus(voucherValidRange.getValue(), voucherValidRange.getUnit()))
                .voucherStatus(VoucherStatus.PENDING)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private VoucherId id;
        private CustomerId customerId;
        private VoucherPackId voucherPackId;
        private String voucherCode;
        private Discount discount;
        private LocalDateTime redeemedAt;
        private LocalDateTime validTo;
        private VoucherStatus voucherStatus;

        private Builder() {
        }

        public Builder id(String val) {
            id = new VoucherId(UUID.fromString(val));
            return this;
        }

        public Builder customerId(String val) {
            customerId = new CustomerId(UUID.fromString(val));
            return this;
        }

        public Builder voucherPackId(Long val) {
            voucherPackId = new VoucherPackId(val);
            return this;
        }

        public Builder voucherCode(String val) {
            voucherCode = val;
            return this;
        }

        public Builder discount(Discount val) {
            discount = val;
            return this;
        }

        public Builder redeemedAt(LocalDateTime val) {
            redeemedAt = val;
            return this;
        }

        public Builder validTo(LocalDateTime val) {
            validTo = val;
            return this;
        }

        public Builder voucherStatus(VoucherStatus val) {
            voucherStatus = val;
            return this;
        }

        public Voucher build() {
            return new Voucher(this);
        }
    }
}
