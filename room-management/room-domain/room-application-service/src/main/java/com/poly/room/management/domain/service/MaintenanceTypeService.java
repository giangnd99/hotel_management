package com.poly.room.management.domain.service;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;

import java.util.List;

public interface MaintenanceTypeService {
    MaintenanceTypeResponse createMaintenanceType(String name) throws RoomDomainException;
    MaintenanceTypeResponse updateMaintenanceTypeName(Integer maintenanceTypeId, String newName) throws RoomDomainException;
    void deleteMaintenanceType(Integer maintenanceTypeId) throws RoomDomainException;
    MaintenanceTypeResponse getMaintenanceTypeById(Integer maintenanceTypeId) throws RoomDomainException;
    List<MaintenanceTypeResponse> getAllMaintenanceTypes();
}