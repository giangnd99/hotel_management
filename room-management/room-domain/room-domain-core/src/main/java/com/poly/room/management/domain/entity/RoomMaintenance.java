package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.MaintenanceStatus;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.MaintenanceId;

import java.time.LocalDateTime;

public class RoomMaintenance extends BaseEntity<MaintenanceId> {
    private final Room room;
    private StaffId staffId;
    private final DateCustom scheduledDate;
    private DateCustom startDate;
    private DateCustom completionDate;
    private final MaintenanceType maintenanceType;
    private String description;
    private MaintenanceStatus status;

    private RoomMaintenance(Builder builder) {
        super.setId(builder.id);
        this.room = builder.room;
        this.staffId = builder.staffId;
        this.scheduledDate = builder.scheduledDate;
        this.maintenanceType = builder.maintenanceType;
        this.startDate = builder.startDate;
        this.completionDate = builder.completionDate;
        this.description = builder.description;
        this.status = builder.status != null ? builder.status : MaintenanceStatus.PENDING;
    }

    private void validateMaintenanceRequest() {
        validateRequiredFields();
        validateScheduledDate();
    }

    private void validateRequiredFields() {
        if (room == null) {
            throw new RoomDomainException("Room must be specified for maintenance");
        }
        if (maintenanceType == null) {
            throw new RoomDomainException("Maintenance type must be specified");
        }
        if (scheduledDate == null) {
            throw new RoomDomainException("Maintenance schedule date must be specified");
        }
        if (status == null) {
            throw new RoomDomainException("Maintenance status must be specified");
        }
    }

    private void validateScheduledDate() {
        if (scheduledDate.isBefore(DateCustom.now())) {
            throw new RoomDomainException("Maintenance cannot be scheduled in the past");
        }
    }

    public void startMaintenance() {
        validateStatusTransition(MaintenanceStatus.PENDING, "start");
        status = MaintenanceStatus.IN_PROGRESS;
        startDate = DateCustom.now();
    }

    public void completeMaintenance() {
        validateStatusTransition(MaintenanceStatus.IN_PROGRESS, "complete");
        status = MaintenanceStatus.COMPLETED;
        completionDate = DateCustom.now();
    }

    public void cancelMaintenance() {
        if (status == MaintenanceStatus.COMPLETED || status == MaintenanceStatus.CANCELED) {
            throw new RoomDomainException("Cannot cancel maintenance in " + status + " status");
        }
        status = MaintenanceStatus.CANCELED;
    }

    private void validateStatusTransition(MaintenanceStatus requiredStatus, String operation) {
        if (status != requiredStatus) {
            throw new RoomDomainException("Cannot " + operation + " maintenance in " + status + " status");
        }
    }

    public void assignStaff(StaffId newStaffId) {
        if (newStaffId == null) {
            throw new RoomDomainException("Staff ID cannot be null");
        }
        this.staffId = newStaffId;
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
    }

    public Room getRoom() {
        return room;
    }

    public StaffId getStaffId() {
        return staffId;
    }

    public DateCustom getScheduledDate() {
        return scheduledDate;
    }

    public DateCustom getStartDate() {
        return startDate;
    }

    public DateCustom getCompletionDate() {
        return completionDate;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public String getDescription() {
        return description;
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public static final class Builder {
        private MaintenanceId id;
        private Room room;
        private StaffId staffId;
        private DateCustom scheduledDate;
        private MaintenanceType maintenanceType;
        private String description;
        private MaintenanceStatus status;
        private DateCustom startDate;
        private DateCustom completionDate;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(MaintenanceId val) {
            id = val;
            return this;
        }

        public Builder room(Room val) {
            room = val;
            return this;
        }

        public Builder staffId(StaffId val) {
            staffId = val;
            return this;
        }

        public Builder scheduledDate(DateCustom val) {
            scheduledDate = val;
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

        public Builder status(MaintenanceStatus val) {
            status = val;
            return this;
        }

        public Builder startDate(DateCustom val) {
            startDate = val;
            return this;
        }

        public Builder completionDate(DateCustom val) {
            completionDate = val;
            return this;
        }

        public RoomMaintenance build() {
            return new RoomMaintenance(this);
        }
    }
}