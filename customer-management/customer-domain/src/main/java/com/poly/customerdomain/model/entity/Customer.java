package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.exception.CustomerDomainException;
import com.poly.customerdomain.model.exception.ErrorDomainCode;
import com.poly.customerdomain.model.valueobject.*;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
//@Builder
public class Customer extends AggregateRoot<CustomerId> {

    private UUID userId;
    private Name name;
    private Address address;
    private LocalDate dateOfBirth;
    private Nationality nationality;
    private CustomerType customerType;
    private Money accumulatedSpending;
    private Level level;
    private BehaviorData behaviorData;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Customer(Builder builder) {
        super.setId(builder.id);
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

//    @Builder
//    private Customer(UUID userId, Name name, Address address, LocalDate dateOfBirth, Nationality nationality,
//                     CustomerType customerType, Money accumulatedSpending, Level level, BehaviorData behaviorData,
//                     LocalDateTime createdAt, LocalDateTime updatedAt, CustomerId customerId) {
//
//        if (userId == null) {
//            throw new CustomerDomainException(ErrorDomainCode.USERID_INVALID);
//        }
//
//        if (name == null) {
//            throw new CustomerDomainException(ErrorDomainCode.NAME_INVALID);
//        }
//
//        super.setId(customerId != null ? customerId : new CustomerId(UUID.randomUUID()));
//        this.userId = userId;
//        this.name = name;
//        this.address = address;
//        this.dateOfBirth = dateOfBirth;
//        this.nationality = nationality;
//        this.customerType = customerType;
//        this.accumulatedSpending = accumulatedSpending;
//        this.level = level;
//        this.behaviorData = behaviorData;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }



    public void updateInfo(Name name, Address address, LocalDate dateOfBirth, Nationality nationality) {

        if (name != null) this.name = name;
        if (address != null) this.address = address;
        if (dateOfBirth != null) this.dateOfBirth = dateOfBirth;
        if (nationality != null) this.nationality = nationality;

        this.setUpdatedAt(LocalDateTime.now());
    }

    public static final class Builder {
        private CustomerId id;
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

        public Builder id(CustomerId val) {
            id = val;
            return this;
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

        public Builder dateOfBirth(LocalDate val) {
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

        public Builder updatedAt(LocalDateTime val) {
            updatedAt = val;
            return this;
        }

        public Builder createddAt(LocalDateTime val) {
            createdAt = val;
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