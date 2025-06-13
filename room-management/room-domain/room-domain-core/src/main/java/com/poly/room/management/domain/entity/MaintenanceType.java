package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.MaintenanceTypeId;

public class MaintenanceType extends BaseEntity<MaintenanceTypeId> {

    private String name;

    public MaintenanceType() {
    }

    public MaintenanceType(String name) {
        this.name = name;
        validate();
    }

    private MaintenanceType(Builder builder) {
        super.setId(builder.id);
        setName(builder.name);
    }

    public void validate() {
        if (name.isEmpty() || name == null) {
            throw new RoomDomainException("name is null or empty");
        }
        name = name.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
        validate();
    }

    public static final class Builder {
        private MaintenanceTypeId id;
        private String name;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(MaintenanceTypeId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public MaintenanceType build() {
            return new MaintenanceType(this);
        }
    }
}
