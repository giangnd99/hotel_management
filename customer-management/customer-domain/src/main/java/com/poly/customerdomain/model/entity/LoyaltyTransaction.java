package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.domain.valueobject.CustomerId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LoyaltyTransaction {

    private TransactionId id;
    private LoyaltyId loyaltyId;
    private PointChanged pointChanged;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private Description description;

    private LoyaltyTransaction(Builder builder) {
        id = builder.id;
        loyaltyId = builder.loyaltyId;
        pointChanged = builder.pointChanged;
        transactionType = builder.transactionType;
        transactionDate = builder.transactionDate;
        description = builder.description;
    }

    public static LoyaltyTransaction createNew(
            LoyaltyId loyaltyId,
            PointChanged pointChanged,
            TransactionType transactionType,
            LocalDateTime transactionDate,
            Description description
    ) {
        return new Builder(loyaltyId, pointChanged, transactionType, transactionDate, description)
                .id(TransactionId.generateId())
                .build();
    }

    public static final class Builder {
        private TransactionId id;
        private LoyaltyId loyaltyId;
        private PointChanged pointChanged;
        private TransactionType transactionType;
        private LocalDateTime transactionDate;
        private Description description;

        public Builder(LoyaltyId loyaltyId, PointChanged pointChanged, TransactionType transactionType, LocalDateTime transactionDate, Description description) {
            this.loyaltyId = loyaltyId;
            this.pointChanged = pointChanged;
            this.transactionType = transactionType;
            this.transactionDate = transactionDate;
            this.description = description;
        }

        public Builder id(TransactionId val) {
            id = val;
            return this;
        }

        public Builder loyaltyId(LoyaltyId val) {
            loyaltyId = val;
            return this;
        }

        public Builder pointChanged(PointChanged val) {
            pointChanged = val;
            return this;
        }

        public Builder transactionType(TransactionType val) {
            transactionType = val;
            return this;
        }

        public Builder transactionDate(LocalDateTime val) {
            transactionDate = val;
            return this;
        }

        public Builder description(Description val) {
            description = val;
            return this;
        }

        public LoyaltyTransaction build() {
            return new LoyaltyTransaction(this);
        }
    }
}
