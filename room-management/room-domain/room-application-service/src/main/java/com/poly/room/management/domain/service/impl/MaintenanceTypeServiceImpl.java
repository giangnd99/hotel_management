package com.poly.room.management.domain.service.impl;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.response.MaintenanceTypeResponse;
import com.poly.room.management.domain.service.MaintenanceTypeService;
import com.poly.room.management.domain.port.out.repository.MaintenanceTypeRepository;
import com.poly.room.management.domain.entity.MaintenanceType;
import com.poly.room.management.domain.mapper.MaintenanceTypeDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceTypeServiceImpl implements MaintenanceTypeService {

    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final MaintenanceTypeDtoMapper maintenanceTypeDtoMapper;

    @Override
    public MaintenanceTypeResponse createMaintenanceType(String name) throws RoomDomainException {
        log.info("Creating maintenance type with name: {}", name);
        
        try {
            // Tạo maintenance type mới
            MaintenanceType maintenanceType = MaintenanceType.Builder.builder()
                    .name(name)
                    .build();
            
            MaintenanceType savedMaintenanceType = maintenanceTypeRepository.save(maintenanceType);
            
            log.info("Maintenance type created successfully with ID: {}", savedMaintenanceType.getId().getValue());
            return maintenanceTypeDtoMapper.toResponse(savedMaintenanceType);
            
        } catch (Exception e) {
            log.error("Error creating maintenance type: {}", e.getMessage());
            throw new RoomDomainException("Failed to create maintenance type: " + e.getMessage());
        }
    }

    @Override
    public MaintenanceTypeResponse updateMaintenanceTypeName(Integer maintenanceTypeId, String newName) throws RoomDomainException {
        log.info("Updating maintenance type {} with new name: {}", maintenanceTypeId, newName);
        
        try {
            MaintenanceType maintenanceType = maintenanceTypeRepository.findById(maintenanceTypeId)
                    .orElseThrow(() -> new RoomDomainException("Maintenance type not found with ID: " + maintenanceTypeId));
            
            maintenanceType.setName(newName);
            MaintenanceType updatedMaintenanceType = maintenanceTypeRepository.update(maintenanceType);
            
            log.info("Maintenance type updated successfully: {}", maintenanceTypeId);
            return maintenanceTypeDtoMapper.toResponse(updatedMaintenanceType);
            
        } catch (Exception e) {
            log.error("Error updating maintenance type: {}", e.getMessage());
            throw new RoomDomainException("Failed to update maintenance type: " + e.getMessage());
        }
    }

    @Override
    public void deleteMaintenanceType(Integer maintenanceTypeId) throws RoomDomainException {
        log.info("Deleting maintenance type with ID: {}", maintenanceTypeId);
        
        try {
            MaintenanceType maintenanceType = maintenanceTypeRepository.findById(maintenanceTypeId)
                    .orElseThrow(() -> new RoomDomainException("Maintenance type not found with ID: " + maintenanceTypeId));
            
            maintenanceTypeRepository.delete(maintenanceType);
            
            log.info("Maintenance type deleted successfully: {}", maintenanceTypeId);
            
        } catch (Exception e) {
            log.error("Error deleting maintenance type: {}", e.getMessage());
            throw new RoomDomainException("Failed to delete maintenance type: " + e.getMessage());
        }
    }

    @Override
    public MaintenanceTypeResponse getMaintenanceTypeById(Integer maintenanceTypeId) throws RoomDomainException {
        log.info("Getting maintenance type by ID: {}", maintenanceTypeId);
        
        try {
            MaintenanceType maintenanceType = maintenanceTypeRepository.findById(maintenanceTypeId)
                    .orElseThrow(() -> new RoomDomainException("Maintenance type not found with ID: " + maintenanceTypeId));
            
            return maintenanceTypeDtoMapper.toResponse(maintenanceType);
            
        } catch (Exception e) {
            log.error("Error getting maintenance type: {}", e.getMessage());
            throw new RoomDomainException("Failed to get maintenance type: " + e.getMessage());
        }
    }

    @Override
    public List<MaintenanceTypeResponse> getAllMaintenanceTypes() {
        log.info("Getting all maintenance types");
        
        try {
            List<MaintenanceType> maintenanceTypes = maintenanceTypeRepository.findAll();
            
            return maintenanceTypes.stream()
                    .map(maintenanceTypeDtoMapper::toResponse)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            log.error("Error getting all maintenance types: {}", e.getMessage());
            return List.of();
        }
    }
}
