package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.promotion.domain.core.exception.PromotionDomainException;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.DiscountAmount;
import com.poly.promotion.domain.core.valueobject.DiscountPercentage;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public static Builder builder() {
        return new Builder();
    }

    private VoucherPack(Builder builder) {
        super.setId(builder.id);
        setDescription(builder.description);
        setDiscountAmount(builder.discount);
        setVoucherValidRange(builder.validRange);
        setRequiredPoints(builder.requiredPoints);
        setQuantity(builder.quantity);
        setPackValidFrom(builder.validFrom);
        setPackValidTo(builder.validTo);
        setCreatedAt(builder.createdAt);
        setCreatedBy(builder.createdBy);
        setUpdatedAt(builder.updatedAt);
        setUpdatedBy(builder.updatedBy);
        setStatus(builder.status);
    }

    public static final class Builder {
        private VoucherPackId id;
        private String description;
        private Discount discount;
        private DateRange validRange;
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
            if (val == null) {return this;}
            if(val>= 0 && val <= 100) {
                discount = new DiscountPercentage(val);
            } else if(val >= 1000) {
                discount = new DiscountAmount(new Money(BigDecimal.valueOf(val)));
            } else {
                throw new PromotionDomainException("Invalid discount amount: " + val);
            }
            return this;
        }

        public Builder validRange(String val) {
            if (val != null && !val.isBlank()) {
                String[] parts = val.split(" ");
                if (parts.length >= 2) {
                    throw new PromotionDomainException("Invalid date range format. Expected format: '<value> <unit>'");
                }
                try{
                    int value = Integer.parseInt(parts[0]);
                    ChronoUnit unit = ChronoUnit.valueOf(parts[1]);
                    validRange = new DateRange(value, unit);
                } catch (IllegalArgumentException e) {
                    throw new PromotionDomainException("Invalid date range format. Expected format: '<value: int> <unit: ChronoUnit string>'", e);
                }

            }
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

        public Builder status(String val) {
            status = VoucherPackStatus.fromString(val);
            return this;
        }

        public VoucherPack build() {
            return new VoucherPack(this);
        }
    }
}
