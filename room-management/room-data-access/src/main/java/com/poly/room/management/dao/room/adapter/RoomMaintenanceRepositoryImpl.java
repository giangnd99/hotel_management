package com.poly.room.management.dao.room.adapter;

import com.poly.room.management.dao.room.entity.RoomMaintenanceEntity;
import com.poly.room.management.dao.room.mapper.RoomMaintenanceMapper;
import com.poly.room.management.dao.room.mapper.MaintenanceTypeMapper;
import com.poly.room.management.dao.room.repository.RoomMaintenanceJpaRepository;
import com.poly.room.management.domain.entity.RoomMaintenance;
import com.poly.room.management.domain.port.out.repository.MaintenanceTypeRepository;
import com.poly.room.management.domain.port.out.repository.RoomMaintenanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomMaintenanceRepositoryImpl implements RoomMaintenanceRepository {

    private final RoomMaintenanceJpaRepository jpaRepository;
    private final RoomMaintenanceMapper roomMaintenanceMapper;
    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final MaintenanceTypeMapper maintenanceTypeMapper;

    // ========== BASIC CRUD OPERATIONS ==========

    @Override
    public RoomMaintenance save(RoomMaintenance roomMaintenance) {
        RoomMaintenanceEntity entity = roomMaintenanceMapper.toJpaEntity(roomMaintenance);
        RoomMaintenanceEntity savedEntity = jpaRepository.save(entity);
        return roomMaintenanceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RoomMaintenance> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(roomMaintenanceMapper::toDomain);
    }

    @Override
    public List<RoomMaintenance> findAll() {
        return jpaRepository.findAll().stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public RoomMaintenance update(RoomMaintenance roomMaintenance) {
        RoomMaintenanceEntity entity = roomMaintenanceMapper.toJpaEntity(roomMaintenance);
        RoomMaintenanceEntity updatedEntity = jpaRepository.save(entity);
        return roomMaintenanceMapper.toDomain(updatedEntity);
    }

    // ========== ROOM-SPECIFIC QUERIES ==========

    @Override
    public List<RoomMaintenance> findByRoomId(UUID roomId) {
        return jpaRepository.findByRoom_RoomId(roomId).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByRoomIdAndStatus(UUID roomId, String status) {
        return jpaRepository.findByRoom_RoomIdAndStatus(roomId, status).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== STATUS-BASED QUERIES ==========

    @Override
    public List<RoomMaintenance> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByStatusWithPagination(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatus(status, pageable).getContent().stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== DATE-BASED QUERIES ==========

    @Override
    public List<RoomMaintenance> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByScheduledDateBetween(
                Timestamp.valueOf(startDate), 
                Timestamp.valueOf(endDate)
        ).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByCompletedDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaRepository.findByCompletedDateBetween(
                Timestamp.valueOf(startDate), 
                Timestamp.valueOf(endDate)
        ).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== MAINTENANCE TYPE QUERIES ==========

    @Override
    public List<RoomMaintenance> findByMaintenanceType(String maintenanceType) {
        // Find maintenance type by name first
        var maintenanceTypeDomain = maintenanceTypeRepository.findByName(maintenanceType)
                .orElse(null);
        
        if (maintenanceTypeDomain == null) {
            return List.of();
        }
        
        // Convert domain to JPA entity for query
        var maintenanceTypeEntity = maintenanceTypeMapper.toEntity(maintenanceTypeDomain);
        
        return jpaRepository.findByMaintenanceType(maintenanceTypeEntity).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByMaintenanceTypeAndStatus(String maintenanceType, String status) {
        // Find maintenance type by name first
        var maintenanceTypeDomain = maintenanceTypeRepository.findByName(maintenanceType)
                .orElse(null);
        
        if (maintenanceTypeDomain == null) {
            return List.of();
        }
        
        // Convert domain to JPA entity for query
        var maintenanceTypeEntity = maintenanceTypeMapper.toEntity(maintenanceTypeDomain);
        
        return jpaRepository.findByMaintenanceTypeAndStatus(maintenanceTypeEntity, status).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== PRIORITY-BASED QUERIES ==========

    @Override
    public List<RoomMaintenance> findByPriority(String priority) {
        return jpaRepository.findByPriority(priority).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByPriorityAndStatus(String priority, String status) {
        return jpaRepository.findByPriorityAndStatus(priority, status).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== STAFF ASSIGNMENT QUERIES ==========

    @Override
    public List<RoomMaintenance> findByAssignedTo(String staffId) {
        return jpaRepository.findByAssignedTo(staffId).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findByAssignedToAndStatus(String staffId, String status) {
        return jpaRepository.findByAssignedToAndStatus(staffId, status).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== PAGINATION ==========

    @Override
    public List<RoomMaintenance> findAllWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findAll(pageable).getContent().stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== STATISTICS ==========

    @Override
    public Long countByStatus(String status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public Long countByMaintenanceType(String maintenanceType) {
        // Find maintenance type by name first
        var maintenanceTypeDomain = maintenanceTypeRepository.findByName(maintenanceType)
                .orElse(null);
        
        if (maintenanceTypeDomain == null) {
            return 0L;
        }
        
        // Convert domain to JPA entity for query
        var maintenanceTypeEntity = maintenanceTypeMapper.toEntity(maintenanceTypeDomain);
        
        return jpaRepository.countByMaintenanceType(maintenanceTypeEntity);
    }

    @Override
    public Long countByPriority(String priority) {
        return jpaRepository.countByPriority(priority);
    }

    // ========== ACTIVE MAINTENANCE ==========

    @Override
    public List<RoomMaintenance> findActiveMaintenance() {
        return jpaRepository.findActiveMaintenance().stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomMaintenance> findOverdueMaintenance(LocalDateTime currentDate) {
        return jpaRepository.findOverdueMaintenance(currentDate).stream()
                .map(roomMaintenanceMapper::toDomain)
                .collect(Collectors.toList());
    }
}