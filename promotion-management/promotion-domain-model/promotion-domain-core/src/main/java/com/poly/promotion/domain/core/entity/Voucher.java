package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.CustomerId;
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

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher extends BaseEntity<VoucherId> {
    CustomerId customerId;
    VoucherPackId voucherPackId;
    String voucherCode;
    Double discountAmount;
    LocalDateTime redeemedAt;
    LocalDateTime validTo;
    VoucherStatus voucherStatus;

    private Voucher(Builder builder) {
        super.setId(builder.id);
        setCustomerId(builder.customerId);
        setVoucherPackId(builder.voucherPackId);
        setVoucherCode(builder.voucherCode);
        setDiscountAmount(builder.discountAmount);
        setRedeemedAt(builder.redeemedAt);
        setValidTo(builder.validTo);
        setVoucherStatus(builder.voucherStatus);
    }

    public static final class Builder {
        private VoucherId id;
        private CustomerId customerId;
        private VoucherPackId voucherPackId;
        private String voucherCode;
        private Double discountAmount;
        private LocalDateTime redeemedAt;
        private LocalDateTime validTo;
        private VoucherStatus voucherStatus;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder id(UUID val) {
            id = new VoucherId(val);
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
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

        public Builder discountAmount(Double val) {
            discountAmount = val;
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

        public Builder voucherStatus(Integer val) {
            voucherStatus = VoucherStatus.fromStatusCode(val);
            return this;
        }

        public Voucher build() {
            return new Voucher(this);
        }
    }
}
