package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.Money;
import com.poly.promotion.domain.core.exception.PromotionDomainException;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.DiscountAmount;
import com.poly.promotion.domain.core.valueobject.DiscountPercentage;
import com.poly.promotion.domain.core.valueobject.PromotionId;
import com.poly.promotion.domain.core.valueobject.PromotionStatus;
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

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Promotion extends BaseEntity<PromotionId> {
    String name;
    String description;
    Discount discount;
    String target;
    String condition;
    LocalDate startDate;
    LocalDate endDate;
    PromotionStatus status;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime updatedAt;
    String updatedBy;

    private Promotion(Builder builder) {
        setName(builder.name);
        setDescription(builder.description);
        setDiscount(builder.discount);
        setTarget(builder.target);
        setCondition(builder.condition);
        setStartDate(builder.startDate);
        setEndDate(builder.endDate);
        setStatus(builder.status);
        setCreatedAt(builder.createdAt);
        setCreatedBy(builder.createdBy);
        setUpdatedAt(builder.updatedAt);
        setUpdatedBy(builder.updatedBy);
        super.setId(builder.id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String description;
        private Discount discount;
        private String target;
        private String condition;
        private LocalDate startDate;
        private LocalDate endDate;
        private PromotionStatus status;
        private LocalDateTime createdAt;
        private String createdBy;
        private LocalDateTime updatedAt;
        private String updatedBy;
        private PromotionId id;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder discount(Double val) {
            if(val>= 0 && val <= 100) {
                discount = new DiscountPercentage(val);
            } else if(val >= 1000) {
                discount = new DiscountAmount(new Money(BigDecimal.valueOf(val)));
            } else {
                throw new PromotionDomainException("Invalid discount amount: " + val);
            }
            return this;
        }

        public Builder target(String val) {
            target = val;
            return this;
        }

        public Builder condition(String val) {
            condition = val;
            return this;
        }

        public Builder startDate(LocalDate val) {
            startDate = val;
            return this;
        }

        public Builder endDate(LocalDate val) {
            endDate = val;
            return this;
        }

        public Builder status(PromotionStatus val) {
            status = val;
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

        public Builder id(Long val) {
            id = new PromotionId(val);
            return this;
        }

        public Promotion build() {
            return new Promotion(this);
        }
    }

}
