package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Customer extends AggregateRoot<CustomerId> {

    private UUID userId;
    private Name fullName;
    private Address address;
    private DateOfBirth dateOfBirth;
    private Money accumulatedSpending;
    private Level level;
    private ImageUrl image;
    private BehaviorData behaviorData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Customer(Builder builder) {
        this.setId(builder.customerId);
        this.userId = builder.userId != null ? builder.userId : null;
        this.fullName = builder.name != null ? builder.name : Name.empty();
        this.address = builder.address != null ? builder.address : Address.empty();
        this.level = builder.level != null ? builder.level : Level.NONE;
        this.dateOfBirth = builder.dateOfBirth != null ? builder.dateOfBirth : DateOfBirth.empty();
        this.accumulatedSpending = builder.accumulatedSpending != null ? builder.accumulatedSpending : Money.ZERO;
        this.behaviorData = builder.behaviorData != null ? builder.behaviorData : BehaviorData.empty();
        this.image = builder.image != null ? builder.image : null;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
    }

    public static final class Builder {
        private CustomerId customerId;
        private UUID userId;
        private Name name;
        private Address address;
        private DateOfBirth dateOfBirth;
        private Money accumulatedSpending;
        private Level level;
        private ImageUrl image;
        private BehaviorData behaviorData;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder() {
        }

        public Builder customerId(CustomerId id) {
            this.customerId = id;
            return this;
        }

        public Builder userId(UUID val) {
            userId = val;
            return this;
        }

        public Builder image(ImageUrl val) {
            image = val;
            return this;
        }

        public Builder name(Name val) {
            name = val;
            return this;
        }

        public Builder address(Address val) {
            address = val;
            return this;
        }

        public Builder dateOfBirth(DateOfBirth val) {
            dateOfBirth = val;
            return this;
        }

        public Builder accumulatedSpending(Money val) {
            accumulatedSpending = val;
            return this;
        }

        public Builder level(Level val) {
            level = val;
            return this;
        }

        public Builder behaviorData(BehaviorData val) {
            behaviorData = val;
            return this;
        }

        public Builder createdAt(LocalDateTime val) {
            createdAt = val;
            return this;
        }

        public Builder updatedAt(LocalDateTime val) {
            updatedAt = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }


}