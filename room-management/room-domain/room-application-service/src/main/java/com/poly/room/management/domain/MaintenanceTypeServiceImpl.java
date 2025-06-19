package com.poly.room.management.application.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;
import com.poly.room.management.domain.port.in.service.MaintenanceTypeService;
import com.poly.room.management.domain.service.RoomDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceTypeServiceImpl implements MaintenanceTypeService {
    private final RoomDomainService roomDomainService;

    @Override
    @Transactional
    public MaintenanceTypeResponse createMaintenanceType(String name) throws ApplicationServiceException {
        return roomDomainService.createMaintenanceType(name);
    }

    @Override
    @Transactional
    public MaintenanceTypeResponse updateMaintenanceTypeName(Integer maintenanceTypeId, String newName) throws ApplicationServiceException {
        return roomDomainService.updateMaintenanceTypeName(maintenanceTypeId, newName);
    }

    @Override
    @Transactional
    public void deleteMaintenanceType(Integer maintenanceTypeId) throws ApplicationServiceException {
        roomDomainService.deleteMaintenanceType(maintenanceTypeId);
    }

    @Override
    public MaintenanceTypeResponse getMaintenanceTypeById(Integer maintenanceTypeId) throws ApplicationServiceException {
        return roomDomainService.getMaintenanceTypeById(maintenanceTypeId);
    }

    @Override
    public List<MaintenanceTypeResponse> getAllMaintenanceTypes() {
        return roomDomainService.getAllMaintenanceTypes();
    }
}