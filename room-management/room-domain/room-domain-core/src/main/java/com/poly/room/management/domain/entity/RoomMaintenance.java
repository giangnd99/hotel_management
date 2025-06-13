package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.MaintenanceStatus;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.valueobject.MaintenanceId;

import java.sql.Timestamp;
import java.time.Instant;

public class RoomMaintenance extends BaseEntity<MaintenanceId> {

    private Room room;
    private StaffId staffId;
    private Timestamp maintenanceDate; // Ngày yêu cầu/lên lịch bảo trì
    private Timestamp actualStartDate; // Ngày thực tế bắt đầu bảo trì
    private Timestamp completionDate;  // Ngày thực tế hoàn thành bảo trì
    private MaintenanceType maintenanceType;
    private String description;
    private MaintenanceStatus maintenanceStatus; // Trạng thái của yêu cầu bảo trì

    /**
     * Constructor mặc định.
     */
    public RoomMaintenance() {
        // Constructor mặc định cho các framework ORM
    }

    /**
     * Constructor đầy đủ để tạo một yêu cầu bảo trì.
     *
     * @param room              ID của phòng cần bảo trì.
     * @param staffId           ID của nhân viên chịu trách nhiệm.
     * @param maintenanceDate   Ngày yêu cầu bảo trì.
     * @param maintenanceType   Loại hình bảo trì.
     * @param description       Mô tả chi tiết.
     * @param maintenanceStatus Trạng thái ban đầu của yêu cầu bảo trì.
     */
    public RoomMaintenance(Room room,
                           StaffId staffId,
                           Timestamp maintenanceDate,
                           MaintenanceType maintenanceType,
                           String description,
                           MaintenanceStatus maintenanceStatus) {
        this.room = room;
        this.staffId = staffId;
        this.maintenanceDate = maintenanceDate;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.maintenanceStatus = maintenanceStatus;
        validate(); // Validate ngay khi khởi tạo
    }

    // Private constructor cho Builder
    private RoomMaintenance(Builder builder) {
        super.setId(builder.id);
        this.room = builder.room;
        this.staffId = builder.staffId;
        this.maintenanceDate = builder.maintenanceDate;
        this.maintenanceType = builder.maintenanceType;
        this.description = builder.description;
        // Đảm bảo trạng thái mặc định nếu không được cung cấp từ builder
        this.maintenanceStatus = builder.maintenanceStatus != null ? builder.maintenanceStatus : MaintenanceStatus.PENDING;
        validate(); // Validate ngay khi xây dựng
    }

    /**
     * Phương thức validate nghiệp vụ cho entity RoomMaintenance.
     * Đảm bảo các thuộc tính bắt buộc không null.
     *
     * @throws RoomDomainException Nếu dữ liệu không hợp lệ.
     */
    public void validate() {
        if (room == null) {
            throw new RoomDomainException("RoomId cannot be null for RoomMaintenance.");
        }
        if (maintenanceType == null) {
            throw new RoomDomainException("MaintenanceType cannot be null for RoomMaintenance.");
        }
        if (maintenanceDate == null) {
            throw new RoomDomainException("MaintenanceDate cannot be null for RoomMaintenance.");
        }
        if (maintenanceStatus == null) {
            throw new RoomDomainException("MaintenanceStatus cannot be null for RoomMaintenance.");
        }
        // StaffId và description có thể null tùy theo nghiệp vụ
    }

    /**
     * Bắt đầu quá trình bảo trì.
     * Chuyển trạng thái từ PENDING sang IN_PROGRESS và ghi lại thời gian bắt đầu thực tế.
     *
     * @throws RoomDomainException Nếu trạng thái hiện tại không cho phép bắt đầu bảo trì.
     */
    public void startMaintenance() {
        if (this.maintenanceStatus != MaintenanceStatus.PENDING) {
            throw new RoomDomainException("Cannot start maintenance from current status: " + this.maintenanceStatus.name());
        }
        this.maintenanceStatus = MaintenanceStatus.IN_PROGRESS;
        this.actualStartDate = Timestamp.from(Instant.now());
    }

    /**
     * Hoàn thành quá trình bảo trì.
     * Chuyển trạng thái từ IN_PROGRESS sang COMPLETED và ghi lại thời gian hoàn thành.
     *
     * @throws RoomDomainException Nếu trạng thái hiện tại không cho phép hoàn thành bảo trì.
     */
    public void completeMaintenance() {
        if (this.maintenanceStatus != MaintenanceStatus.IN_PROGRESS) {
            throw new RoomDomainException("Cannot complete maintenance from current status: " + this.maintenanceStatus.name());
        }
        this.maintenanceStatus = MaintenanceStatus.COMPLETED;
        this.completionDate = Timestamp.from(Instant.now());
    }

    /**
     * Hủy yêu cầu bảo trì.
     * Có thể hủy từ PENDING hoặc IN_PROGRESS.
     *
     * @throws RoomDomainException Nếu trạng thái hiện tại không cho phép hủy bảo trì.
     */
    public void cancelMaintenance() {
        if (this.maintenanceStatus == MaintenanceStatus.COMPLETED || this.maintenanceStatus == MaintenanceStatus.CANCELED) {
            throw new RoomDomainException("Cannot cancel maintenance from current status: " + this.maintenanceStatus.name());
        }
        this.maintenanceStatus = MaintenanceStatus.CANCELED;
    }

    /**
     * Gán nhân viên cho yêu cầu bảo trì.
     *
     * @param staffId ID của nhân viên mới.
     */
    public void assignStaff(StaffId staffId) {
        this.staffId = staffId;
    }

    /**
     * Cập nhật mô tả bảo trì.
     *
     * @param description Mô tả mới.
     */
    public void updateDescription(String description) {
        this.description = description;
    }

    // Getters
    public Room getRoom() {
        return room;
    }

    public StaffId getStaffId() {
        return staffId;
    }

    public Timestamp getMaintenanceDate() {
        return maintenanceDate;
    }

    public Timestamp getActualStartDate() {
        return actualStartDate;
    }

    public Timestamp getCompletionDate() {
        return completionDate;
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

    // Builder class
    public static final class Builder {
        private MaintenanceId id;
        private Room room;
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

        public Builder room(Room val) {
            room = val;
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
