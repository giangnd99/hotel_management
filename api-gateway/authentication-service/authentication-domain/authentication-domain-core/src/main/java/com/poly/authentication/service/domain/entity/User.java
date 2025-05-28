package com.poly.authentication.service.domain.entity;

import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.UserId;

public class User extends AggregateRoot<UserId> {

    private String gmail;
    private String password;
    private String phone;

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    //Builder Pattern
    private User(Builder builder) {
        super.setId(builder.userId);
        gmail = builder.gmail;
        password = builder.password;
        phone = builder.phone;
    }


    public static final class Builder {
        private UserId userId;
        private String gmail;
        private String password;
        private String phone;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder userId(UserId val) {
            userId = val;
            return this;
        }

        public Builder gmail(String val) {
            gmail = val;
            return this;
        }

        public Builder password(String val) {
            password = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
