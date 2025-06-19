package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.DateCustom;
import com.poly.domain.valueobject.MaintenanceStatus;
import com.poly.domain.valueobject.RoomId;
import com.poly.domain.valueobject.StaffId;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.service.sub.MaintenanceQueryService;
import com.poly.room.management.domain.valueobject.MaintenanceId;
import com.poly.room.management.domain.valueobject.MaintenanceTypeId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MaintenanceQueryServiceImpl implements MaintenanceQueryService {
    @Override
    public Optional<RoomMaintenance> getRoomMaintenanceById(List<RoomMaintenance> allMaintenances, MaintenanceId maintenanceId) {
        return allMaintenances.stream()
                .filter(rm -> rm.getId().equals(maintenanceId))
                .findFirst();
    }

    @Override
    public List<RoomMaintenance> getAllRoomMaintenances(List<RoomMaintenance> allMaintenances) {
        return allMaintenances;
    }

    @Override
    public List<RoomMaintenance> getRoomMaintenancesByRoomId(List<RoomMaintenance> allMaintenances, RoomId roomId) {
        return allMaintenances.stream()
                .filter(rm -> rm.getRoom().getId().equals(roomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> getRoomMaintenancesByStaffId(List<RoomMaintenance> allMaintenances, StaffId staffId) {
        return allMaintenances.stream()
                .filter(rm -> rm.getStaffId() != null && rm.getStaffId().equals(staffId))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> getRoomMaintenancesByStatus(List<RoomMaintenance> allMaintenances, String status) {
        try {
            MaintenanceStatus maintenanceStatus = MaintenanceStatus.valueOf(status.toUpperCase());
            return allMaintenances.stream()
                    .filter(rm -> rm.getStatus() == maintenanceStatus)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RoomDomainException("Invalid maintenance status: " + status);
        }
    }

    @Override
    public List<RoomMaintenance> getRoomMaintenancesBetweenDates(List<RoomMaintenance> allMaintenances,
                                                                 DateCustom startDate, DateCustom endDate) {
        return allMaintenances.stream()
                .filter(rm -> !rm.getScheduledDate().isBefore(startDate) && !rm.getScheduledDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MaintenanceType> getMaintenanceTypeById(List<MaintenanceType> allMaintenanceTypes, MaintenanceTypeId maintenanceTypeId) {
        return allMaintenanceTypes.stream()
                .filter(mt -> mt.getId().equals(maintenanceTypeId))
                .findFirst();
    }

    @Override
    public List<MaintenanceType> getAllMaintenanceTypes(List<MaintenanceType> allMaintenanceTypes) {
        return allMaintenanceTypes;
    }
}