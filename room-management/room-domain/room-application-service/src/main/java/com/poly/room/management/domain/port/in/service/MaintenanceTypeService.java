package com.poly.room.management.domain.port.in.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;

import java.util.List;

public interface MaintenanceTypeService {
    MaintenanceTypeResponse createMaintenanceType(String name) throws ApplicationServiceException;
    MaintenanceTypeResponse updateMaintenanceTypeName(Integer maintenanceTypeId, String newName) throws ApplicationServiceException;
    void deleteMaintenanceType(Integer maintenanceTypeId) throws ApplicationServiceException;
    MaintenanceTypeResponse getMaintenanceTypeById(Integer maintenanceTypeId) throws ApplicationServiceException;
    List<MaintenanceTypeResponse> getAllMaintenanceTypes();
}