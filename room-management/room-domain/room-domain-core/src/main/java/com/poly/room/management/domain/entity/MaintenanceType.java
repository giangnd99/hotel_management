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

    public void validate() {

        if (name.isEmpty() || name == null) {
            throw new RoomDomainException("name is null or empty");
        }
        name = name.trim();
    }

    public String getName() {
        return name;
    }
}
