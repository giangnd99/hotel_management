package com.poly.room.management.domain.valueobject;

import com.poly.domain.valueobject.BaseId;
import java.util.UUID;

public class MaintenanceId extends BaseId<UUID> {
    public MaintenanceId(UUID value) {
        super(value);
    }
}
