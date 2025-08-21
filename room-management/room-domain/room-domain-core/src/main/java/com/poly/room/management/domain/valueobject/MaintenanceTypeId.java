package com.poly.room.management.domain.valueobject;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class MaintenanceTypeId extends BaseId<UUID> {

    public MaintenanceTypeId(UUID value) {
        super(value);
    }
}
