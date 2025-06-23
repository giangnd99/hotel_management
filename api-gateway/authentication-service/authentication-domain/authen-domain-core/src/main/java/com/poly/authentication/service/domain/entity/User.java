package com.poly.authentication.service.domain.entity;

import com.poly.authentication.service.domain.exception.AuthenException;
import com.poly.authentication.service.domain.valueobject.Password;
import com.poly.domain.entity.AggregateRoot;
import com.poly.domain.valueobject.UserId;

public class User extends AggregateRoot<UserId> {

    private String gmail;
    private Password password;
    private String phone;
    private Role role;
    private Token token;

    public String getGmail() {
        return gmail;
    }

    public Password getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public Token getToken() {
        return token;
    }

    private User(Builder builder) {
        super.setId(builder.id);
        gmail = builder.gmail;
        password = builder.password;
        phone = builder.phone;
        role = builder.role;
        token = builder.token;
        validate();
    }

    public boolean checkPassword(Password password) {
        return this.password.equals(password);
    }

    public void changePassword(Password password) {
        if (password == null) {
            throw new AuthenException("password is empty");
        } else if (checkPassword(password)) {
            throw new AuthenException("password is same");
        }

        this.password = password;
    }

    public void changeGmail(String gmail) {
        this.gmail = gmail;
    }

    public void changePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new AuthenException("phone is empty");
        } else if (isValidSizePhoneNumber(phone)) {
            throw new AuthenException("phone is invalid");
        } else if (phone.equals(this.phone)) {
            throw new AuthenException("phone is same");
        }
        this.phone = phone;
    }


    private boolean isValidSizePhoneNumber(String phone) {
        return !phone.matches("^\\+?\\d{10,11}$");
    }


    public void validate() {
        checkPassword(password);
        checkGmail(gmail);
        checkPhone(phone);
        checkRole(role);
    }

    private void checkPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new AuthenException("phone is empty");
        }else if (isValidSizePhoneNumber(phone)) {
            throw new AuthenException("phone is invalid");
        }
    }

    public void checkGmail(String gmail) {
        if (gmail == null || gmail.isEmpty()) {
            throw new AuthenException("gmail is empty");
        }
    }

    public void checkRole(Role role) {
        if (role == null) {
            throw new AuthenException("role is empty");
        }
    }

    public static final class Builder {
        private UserId id;
        private String gmail;
        private Password password;
        private String phone;
        private Role role;
        private Token token;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(UserId val) {
            id = val;
            return this;
        }

        public Builder gmail(String val) {
            gmail = val;
            return this;
        }

        public Builder password(Password val) {
            password = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder role(Role val) {
            role = val;
            return this;
        }

        public Builder token(Token val) {
            token = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
