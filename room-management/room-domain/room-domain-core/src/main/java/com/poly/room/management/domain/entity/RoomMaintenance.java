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

    public RoomMaintenance(RoomId roomId,
                           StaffId staffId,
                           Timestamp maintenanceDate,
                           MaintenanceType maintenanceType,
                           String description,
                           MaintenanceStatus maintenanceStatus) {
        this.roomId = roomId;
        this.staffId = staffId;
        this.maintenanceDate = maintenanceDate;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.maintenanceStatus = maintenanceStatus;
    }

    public RoomMaintenance() {
    }
    private RoomMaintenance(Builder builder) {
        super.setId(builder.id);
        roomId = builder.roomId;
        staffId = builder.staffId;
        maintenanceDate = builder.maintenanceDate;
        maintenanceType = builder.maintenanceType;
        description = builder.description;
        maintenanceStatus = builder.maintenanceStatus;
    }

    public RoomId getRoomId() {
        return roomId;
    }

    public StaffId getStaffId() {
        return staffId;
    }

    public Timestamp getMaintenanceDate() {
        return maintenanceDate;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public String getDescription() {
        return description;
    }

    public MaintenanceStatus getMaintenanceStatus() {
        return maintenanceStatus;
    }


    public static final class Builder {
        private MaintenanceId id;
        private RoomId roomId;
        private StaffId staffId;
        private Timestamp maintenanceDate;
        private MaintenanceType maintenanceType;
        private String description;
        private MaintenanceStatus maintenanceStatus;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(MaintenanceId val) {
            id = val;
            return this;
        }

        public Builder roomId(RoomId val) {
            roomId = val;
            return this;
        }

        public Builder staffId(StaffId val) {
            staffId = val;
            return this;
        }

        public Builder maintenanceDate(Timestamp val) {
            maintenanceDate = val;
            return this;
        }

        public Builder maintenanceType(MaintenanceType val) {
            maintenanceType = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder maintenanceStatus(MaintenanceStatus val) {
            maintenanceStatus = val;
            return this;
        }

        public RoomMaintenance build() {
            return new RoomMaintenance(this);
        }
    }
}
