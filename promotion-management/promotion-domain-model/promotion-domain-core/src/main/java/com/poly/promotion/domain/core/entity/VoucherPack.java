package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPack extends BaseEntity<VoucherPackId> {
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
    VoucherPackStatus status;

    public static Builder builder() {
        return new Builder();
    }

    private VoucherPack(Builder builder) {
        super.setId(builder.id);
        setDescription(builder.description);
        setDiscountAmount(builder.discountAmount);
        setValidRange(builder.validRange);
        setRequiredPoints(builder.requiredPoints);
        setQuantity(builder.quantity);
        setValidFrom(builder.validFrom);
        setValidTo(builder.validTo);
        setCreatedAt(builder.createdAt);
        setCreatedBy(builder.createdBy);
        setUpdatedAt(builder.updatedAt);
        setUpdatedBy(builder.updatedBy);
        setStatus(builder.status);
    }

    public static final class Builder {
        private VoucherPackId id;
        private String description;
        private Double discountAmount;
        private String validRange;
        private Long requiredPoints;
        private Integer quantity;
        private LocalDate validFrom;
        private LocalDate validTo;
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime updatedAt;
        private String updatedBy;
        private VoucherPackStatus status;

        private Builder() {
        }

        public Builder id(Long val) {
            id = new VoucherPackId(val);
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder discountAmount(Double val) {
            discountAmount = val;
            return this;
        }

        public Builder validRange(String val) {
            validRange = val;
            return this;
        }

        public Builder requiredPoints(Long val) {
            requiredPoints = val;
            return this;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public Builder validFrom(LocalDate val) {
            validFrom = val;
            return this;
        }

        public Builder validTo(LocalDate val) {
            validTo = val;
            return this;
        }

        public Builder createdAt(LocalDateTime val) {
            createdAt = val;
            return this;
        }

        public Builder createdBy(String val) {
            createdBy = val;
            return this;
        }

        public Builder updatedAt(LocalDateTime val) {
            updatedAt = val;
            return this;
        }

        public Builder updatedBy(String val) {
            updatedBy = val;
            return this;
        }

        public Builder status(Integer val) {
            status = VoucherPackStatus.fromStatusCode(val);
            return this;
        }

        public VoucherPack build() {
            return new VoucherPack(this);
        }
    }
}
