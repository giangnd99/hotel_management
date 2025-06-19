//package com.poly.room.management.domain;
//
//import com.poly.application.handler.ApplicationServiceException;
//import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;
//import com.poly.room.management.domain.entity.MaintenanceType;
//import com.poly.room.management.domain.port.in.service.MaintenanceTypeService;
//import com.poly.room.management.domain.service.RoomDomainService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class MaintenanceTypeServiceImpl implements MaintenanceTypeService {
//    private final RoomDomainService roomDomainService;
//
//    @Override
//    @Transactional
//    public MaintenanceTypeResponse createMaintenanceType(String name) throws ApplicationServiceException {
//        if (name == null || name.trim().isEmpty()) {
//            throw new ApplicationServiceException("Maintenance type name cannot be empty");
//        }
//        try {
//            return roomDomainService.getMaintenanceCommandService().createMaintenanceType(name.trim());
//        } catch (Exception e) {
//            throw new ApplicationServiceException("Failed to create maintenance type: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public MaintenanceTypeResponse updateMaintenanceTypeName(Integer maintenanceTypeId, String newName) throws ApplicationServiceException {
//        if (maintenanceTypeId == null || maintenanceTypeId <= 0) {
//            throw new ApplicationServiceException("Invalid maintenance type ID");
//        }
//        if (newName == null || newName.trim().isEmpty()) {
//            throw new ApplicationServiceException("New name cannot be empty");
//        }
//        try {
//            MaintenanceType maintenanceType = roomDomainService.getMaintenanceQueryService()
//                .findMaintenanceTypeById(maintenanceTypeId)
//                .orElseThrow(() -> new ApplicationServiceException("Maintenance type not found"));
//            return roomDomainService.getMaintenanceCommandService().updateMaintenanceTypeName(maintenanceType, newName.trim());
//        } catch (Exception e) {
//            throw new ApplicationServiceException("Failed to update maintenance type: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void deleteMaintenanceType(Integer maintenanceTypeId) throws ApplicationServiceException {
//        if (maintenanceTypeId == null || maintenanceTypeId <= 0) {
//            throw new ApplicationServiceException("Invalid maintenance type ID");
//        }
//        try {
//            MaintenanceType maintenanceType = roomDomainService.getMaintenanceQueryService()
//                .findMaintenanceTypeById(maintenanceTypeId)
//                .orElseThrow(() -> new ApplicationServiceException("Maintenance type not found"));
//            String userId = "SYSTEM"; // Or get from security context
//            roomDomainService.getMaintenanceCommandService().deleteMaintenanceType(maintenanceType, userId);
//        } catch (Exception e) {
//            throw new ApplicationServiceException("Failed to delete maintenance type: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public MaintenanceTypeResponse getMaintenanceTypeById(Integer maintenanceTypeId) throws ApplicationServiceException {
//        if (maintenanceTypeId == null || maintenanceTypeId <= 0) {
//            throw new ApplicationServiceException("Invalid maintenance type ID");
//        }
//        try {
//            String userId = "SYSTEM"; // Or get from security context
//            return roomDomainService.getMaintenanceQueryService().getMaintenanceTypeById(maintenanceTypeId, userId);
//        } catch (Exception e) {
//            throw new ApplicationServiceException("Failed to get maintenance type: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<MaintenanceTypeResponse> getAllMaintenanceTypes() throws ApplicationServiceException {
//        try {
//            String userId = "SYSTEM"; // Or get from security context
//            return roomDomainService.getMaintenanceQueryService().getAllMaintenanceTypes(userId);
//        } catch (Exception e) {
//            throw new ApplicationServiceException("Failed to get all maintenance types: " + e.getMessage(), e);
//        }
//    }
//}