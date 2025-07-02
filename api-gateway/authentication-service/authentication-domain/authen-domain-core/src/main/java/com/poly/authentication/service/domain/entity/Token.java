package com.poly.authentication.service.domain.entity;

import com.poly.authentication.service.domain.valueobject.TokenId;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.DateCustom;

public class Token extends BaseEntity<TokenId> {

    private DateCustom expiryTime;

    private boolean invalidated = false;

    private boolean expired = false;

    private boolean revoked = false;

    private DateCustom refreshTime;

    private DateCustom createTime;

    public DateCustom getExpiryTime() {
        return expiryTime;
    }

    public void invalidate() {
        expiryTime = null;
    }

    private Token(Builder builder) {
        super.setId(builder.id);
        expiryTime = builder.expiryTime;
        refreshTime = builder.refreshTime;
        createTime = builder.createTime;
    }


    public static final class Builder {
        private DateCustom expiryTime;
        private DateCustom refreshTime;
        private DateCustom createTime;
        private TokenId id;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(TokenId val) {
            id = val;
            return this;
        }

        public Builder expiryTime(DateCustom val) {
            expiryTime = val;
            return this;
        }

        public Builder refreshTime(DateCustom val) {
            refreshTime = val;
            return this;
        }

        public Builder createTime(DateCustom val) {
            createTime = val;
            return this;
        }

        public Token build() {
            return new Token(this);
        }

    }
}
