package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.exception.CustomerDomainException;
import com.poly.customerdomain.model.exception.ErrorDomainCode;
import com.poly.customerdomain.model.valueobject.*;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Customer extends AggregateRoot<CustomerId> {

    private UUID userId;
    private Name name;
    private Address address;
    private LocalDate dateOfBirth;
    private Nationality nationality;
    private CustomerType customerType;
    private Money accumulatedSpending;
    private Level level;
    private final BehaviorData behaviorData;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Customer(Builder builder) {
        super.setId(builder.customerId != null ? builder.customerId : new CustomerId(UUID.randomUUID()));
        this.userId = builder.userId;
        this.name = builder.name;
        this.address = builder.address;
        this.dateOfBirth = builder.dateOfBirth;
        this.nationality = builder.nationality;
        this.customerType = builder.customerType;
        this.accumulatedSpending = builder.accumulatedSpending;
        this.level = builder.level;
        this.behaviorData = builder.behaviorData;
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
    }

    public static class Builder {
        private CustomerId customerId;
        private UUID userId;
        private Name name;
        private Address address;
        private LocalDate dateOfBirth;
        private Nationality nationality;
        private CustomerType customerType;
        private Money accumulatedSpending;
        private Level level;
        private BehaviorData behaviorData;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder() {
            this.accumulatedSpending = new Money(BigDecimal.valueOf(1));
            this.level = Level.None;
        }

        public Builder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public Builder name(Name name) {
            this.name = name;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Builder dateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public Builder nationality(Nationality nationality) {
            this.nationality = nationality;
            return this;
        }

        public Builder customerType(CustomerType customerType) {
            this.customerType = customerType;
            return this;
        }

        public Builder accumulatedSpending(Money accumulatedSpending) {
            this.accumulatedSpending = accumulatedSpending;
            return this;
        }

        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        public Builder behaviorData(BehaviorData behaviorData) {
            this.behaviorData = behaviorData;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Customer build() {
            if (name == null) {
                throw new CustomerDomainException(ErrorDomainCode.NAME_INVALID);
            }
            if (userId == null) {
                throw new CustomerDomainException(ErrorDomainCode.USERID_INVALID);
            }
            return new Customer(this);
        }
    }

    // Getters
    public UUID getUserId() { return userId; }
    public Name getName() { return name; }
    public Address getAddress() { return address; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public Nationality getNationality() { return nationality; }
    public CustomerType getCustomerType() { return customerType; }
    public Money getAccumulatedSpending() { return accumulatedSpending; }
    public Level getLevel() { return level; }
    public BehaviorData getBehaviorData() { return behaviorData; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}