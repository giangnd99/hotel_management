package com.poly.customerdomain.model.entity;

import com.poly.customerdomain.model.entity.valueobject.*;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.CustomerId;
import com.poly.domain.valueobject.Money;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Customer extends AggregateRoot<CustomerId> {

    private UserId userId;
    private Name fullName;
    private Address address;
    private DateOfBirth dateOfBirth;
    private Level level;
    private ImageUrl image;
    private BehaviorData behaviorData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Sex sex;
    private boolean active;

    private Customer(Builder builder) {
        this.setId(builder.customerId);
        this.userId = builder.userId != null ? builder.userId : null;
        this.fullName = builder.name != null ? builder.name : Name.empty();
        this.address = builder.address != null ? builder.address : Address.empty();
        this.level = builder.level != null ? builder.level : Level.NONE;
        this.dateOfBirth = builder.dateOfBirth != null ? builder.dateOfBirth : DateOfBirth.empty();
        this.behaviorData = builder.behaviorData != null ? builder.behaviorData : BehaviorData.empty();
        this.image = builder.image != null ? builder.image : ImageUrl.empty();
        this.createdAt = builder.createdAt != null ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = builder.updatedAt != null ? builder.updatedAt : LocalDateTime.now();
        this.sex = builder.sex;
        this.active = builder.active;

    }

    public static final class Builder {
        private CustomerId customerId;
        private UserId userId;
        private Name name;
        private Address address;
        private DateOfBirth dateOfBirth;
        private Level level;
        private ImageUrl image;
        private BehaviorData behaviorData;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Sex sex;
        private boolean active;

        public Builder() {
        }

        public Builder customerId(CustomerId id) {
            this.customerId = id;
            return this;
        }

        public Builder userId(UserId val) {
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

        public Builder sex(Sex val) {
            sex = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public void upgradeTo(Level level) {
        this.setLevel(level);
    }
}