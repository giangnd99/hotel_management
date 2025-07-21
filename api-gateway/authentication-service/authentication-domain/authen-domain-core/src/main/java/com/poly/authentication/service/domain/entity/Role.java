package com.poly.authentication.service.domain.entity;

import com.poly.authentication.service.domain.valueobject.RoleId;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.ERole;

public class Role extends BaseEntity<RoleId> {

    private ERole role;

    public String getRoleName() {
        return role.name();
    }

    private Role(Builder builder) {
        super.setId(builder.id);
        role = builder.role;
    }


    public static final class Builder {
        private RoleId id;
        private ERole role;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(RoleId val) {
            id = val;
            return this;
        }

        public Builder name(ERole val) {
            role = val;
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }
}
