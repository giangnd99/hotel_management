package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.RoomMaintenance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomMaintenanceRepository {

    // Basic CRUD operations
    RoomMaintenance save(RoomMaintenance roomMaintenance);
    Optional<RoomMaintenance> findById(UUID id);
    List<RoomMaintenance> findAll();
    void deleteById(UUID id);
    RoomMaintenance update(RoomMaintenance roomMaintenance);

    // Room-specific queries
    List<RoomMaintenance> findByRoomId(UUID roomId);
    List<RoomMaintenance> findByRoomIdAndStatus(UUID roomId, String status);
    
    // Status-based queries
    List<RoomMaintenance> findByStatus(String status);
    List<RoomMaintenance> findByStatusWithPagination(String status, int page, int size);
    
    // Date-based queries
    List<RoomMaintenance> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<RoomMaintenance> findByCompletedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Maintenance type queries
    List<RoomMaintenance> findByMaintenanceType(String maintenanceType);
    List<RoomMaintenance> findByMaintenanceTypeAndStatus(String maintenanceType, String status);
    
    // Priority-based queries
    List<RoomMaintenance> findByPriority(String priority);
    List<RoomMaintenance> findByPriorityAndStatus(String priority, String status);
    
    // Staff assignment queries
    List<RoomMaintenance> findByAssignedTo(String staffId);
    List<RoomMaintenance> findByAssignedToAndStatus(String staffId, String status);
    
    // Pagination
    List<RoomMaintenance> findAllWithPagination(int page, int size);
    
    // Statistics
    Long countByStatus(String status);
    Long countByMaintenanceType(String maintenanceType);
    Long countByPriority(String priority);
    
    // Active maintenance
    List<RoomMaintenance> findActiveMaintenance();
    List<RoomMaintenance> findOverdueMaintenance(LocalDateTime currentDate);
}
