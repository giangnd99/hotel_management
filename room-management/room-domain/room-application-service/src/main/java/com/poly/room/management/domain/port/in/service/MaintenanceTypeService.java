package com.poly.room.management.domain.port.in.service;

import com.poly.restaurant.domain.handler.AppException;
import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;

import java.util.List;

public interface MaintenanceTypeService {
    MaintenanceTypeResponse createMaintenanceType(String name) throws AppException;
    MaintenanceTypeResponse updateMaintenanceTypeName(Integer maintenanceTypeId, String newName) throws AppException;
    void deleteMaintenanceType(Integer maintenanceTypeId) throws AppException;
    MaintenanceTypeResponse getMaintenanceTypeById(Integer maintenanceTypeId) throws AppException;
    List<MaintenanceTypeResponse> getAllMaintenanceTypes();
}