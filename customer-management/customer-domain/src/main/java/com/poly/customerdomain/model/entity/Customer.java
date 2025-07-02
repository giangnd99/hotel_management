package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.valueobject.*;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Customer extends AggregateRoot<CustomerId> {

    private UUID userId;
    private Name name;
    private Address address;
    private DateOfBirth dateOfBirth;
    private Nationality nationality;
    private CustomerType customerType;
    private Money accumulatedSpending;
    private Level level;
    private BehaviorData behaviorData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Customer(Builder builder) {
        setUserId(builder.userId);
        setName(builder.name);
        setAddress(builder.address);
        setDateOfBirth(builder.dateOfBirth);
        setNationality(builder.nationality);
        setCustomerType(builder.customerType);
        setAccumulatedSpending(builder.accumulatedSpending);
        setLevel(builder.level);
        setBehaviorData(builder.behaviorData);
        createdAt = builder.createdAt;
        setUpdatedAt(builder.updatedAt);
    }

    public static final class Builder {
        private UUID userId;
        private Name name;
        private Address address;
        private DateOfBirth dateOfBirth;
        private Nationality nationality;
        private CustomerType customerType;
        private Money accumulatedSpending;
        private Level level;
        private BehaviorData behaviorData;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder() {
        }

        public Builder userId(UUID val) {
            userId = val;
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

        public Builder nationality(Nationality val) {
            nationality = val;
            return this;
        }

        public Builder customerType(CustomerType val) {
            customerType = val;
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
            updatedAt = val;
            return this;
        }

        public Builder updatedAt(LocalDateTime val) {
            updatedAt = val;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer(this);
            customer.setId(new CustomerId(UUID.randomUUID()));
            return customer;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}