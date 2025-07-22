package com.poly.authentication.service.domain.entity;

import com.poly.authentication.service.domain.valueobject.PermissionId;
import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.DepartmentId;
import com.poly.domain.valueobject.PositionId;

public class Permission extends BaseEntity<PermissionId> {
    private String name;
    private String description;
    private String url;
    private String method;
    private DepartmentId departmentId;
    private PositionId positionId;

    private Permission(Builder builder) {
        super.setId(builder.id);
        name = builder.name;
        description = builder.description;
        url = builder.url;
        method = builder.method;
        departmentId = builder.departmentId;
        positionId = builder.positionId;
    }

    public static final class Builder {
        private PermissionId id;
        private String name;
        private String description;
        private String url;
        private String method;
        private DepartmentId departmentId;
        private PositionId positionId;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(PermissionId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder method(String val) {
            method = val;
            return this;
        }

        public Builder departmentId(DepartmentId val) {
            departmentId = val;
            return this;
        }

        public Builder positionId(PositionId val) {
            positionId = val;
            return this;
        }

        public Permission build() {
            return new Permission(this);
        }
    }
}
