package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.MaintenanceStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.valueobject.MaintenanceId;

import java.sql.Timestamp;

public class RoomMaintenance extends BaseEntity<MaintenanceId> {

    private RoomId roomId;

    private StaffId staffId;

    private Timestamp maintenanceDate;

    private MaintenanceType maintenanceType;

    private String description;

    private MaintenanceStatus maintenanceStatus;
}
