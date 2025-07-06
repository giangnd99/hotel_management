package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.entity.valueobject.Discount;
import com.poly.customerdomain.model.entity.valueobject.PromotionId;
import com.poly.customerdomain.model.entity.valueobject.VoucherId;
import com.poly.customerdomain.model.entity.valueobject.VoucherStatus;
import com.poly.domain.valueobject.CustomerId;
import lombok.ToString;

import java.time.LocalDate;

@ToString
public class Voucher {

    private final VoucherId id;
    private final CustomerId customerId;
    private final String code;
    private final Discount discount;
    private final LocalDate issueDate;
    private final LocalDate expiryDate;
    private final VoucherStatus status;

    private Voucher(Builder builder) {
        id = builder.id;
        customerId = builder.customerId;
        code = builder.code;
        discount = builder.discount;
        issueDate = builder.issueDate;
        expiryDate = builder.expiryDate;
        status = builder.status;
    }

    public static Voucher createNew(CustomerId customerId) {
        return new Builder(customerId)
                .id(VoucherId.generate())
                .code("Unnamed")
                .discount(Discount.zeroDiscount())
                .issueDate(LocalDate.now())
                .expiryDate(LocalDate.now())
                .status(VoucherStatus.EXPIRED)
                .build();
    }

    public static final class Builder {
        private VoucherId id;
        private final CustomerId customerId;
        private String code;
        private Discount discount;
        private LocalDate issueDate;
        private LocalDate expiryDate;
        private VoucherStatus status;

        public Builder(CustomerId customerId) {
            this.customerId = customerId;
        }

        public Builder id(VoucherId id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder discount(Discount discount) {
            this.discount = discount;
            return this;
        }

        public Builder issueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return this;
        }

        public Builder expiryDate(LocalDate expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public Builder status(VoucherStatus status) {
            this.status = status;
            return this;
        }

        public Voucher build() {
            return new Voucher(this);
        }
    }
}
