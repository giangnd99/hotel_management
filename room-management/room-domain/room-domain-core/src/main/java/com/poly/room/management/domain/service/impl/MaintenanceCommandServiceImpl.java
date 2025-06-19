package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.MaintenanceStatus;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.service.sub.MaintenanceCommandService;

import java.time.LocalDateTime;
import java.util.List;

public class MaintenanceCommandServiceImpl implements MaintenanceCommandService {
    @Override
    public RoomMaintenance createRoomMaintenance(Room room, StaffId staffId, MaintenanceType maintenanceType, String description) {
        RoomMaintenance maintenance = RoomMaintenance.Builder.builder()
                .room(room)
                .staffId(staffId)
                .maintenanceType(maintenanceType)
                .description(description)
                .status(MaintenanceStatus.PENDING)
                .scheduledDate(DateCustom.now())
                .build();

        room.setMaintenanceRoomStatus();
        return maintenance;
    }

    @Override
    public RoomMaintenance startMaintenance(RoomMaintenance roomMaintenance) {
        roomMaintenance.startMaintenance();
        return roomMaintenance;
    }

    @Override
    public RoomMaintenance completeMaintenance(RoomMaintenance roomMaintenance, Room room) {
        roomMaintenance.completeMaintenance();
        room.setVacantRoomStatus();
        return roomMaintenance;
    }

    @Override
    public RoomMaintenance cancelMaintenance(RoomMaintenance roomMaintenance, Room room) {
        roomMaintenance.cancelMaintenance();
        room.setVacantRoomStatus();
        return roomMaintenance;
    }

    @Override
    public RoomMaintenance updateMaintenanceDescription(RoomMaintenance roomMaintenance, String newDescription) {
        roomMaintenance.updateDescription(newDescription);
        return roomMaintenance;
    }

    @Override
    public RoomMaintenance assignStaffToMaintenance(RoomMaintenance roomMaintenance, StaffId newStaffId) {
        roomMaintenance.assignStaff(newStaffId);
        return roomMaintenance;
    }

    @Override
    public MaintenanceType createMaintenanceType(String name) {
        MaintenanceType type = new MaintenanceType(name);
        type.validate();
        return type;
    }

    @Override
    public MaintenanceType updateMaintenanceTypeName(MaintenanceType maintenanceType, String newName) {
        maintenanceType.setName(newName);
        return maintenanceType;
    }

    @Override
    public void deleteMaintenanceType(MaintenanceType maintenanceType, List<RoomMaintenance> allRoomMaintenances) {
        boolean inUse = allRoomMaintenances.stream()
                .anyMatch(rm -> rm.getMaintenanceType().getId().equals(maintenanceType.getId()));
        if (inUse) {
            throw new RoomDomainException("MaintenanceType is in use and cannot be deleted");
        }
    }
}