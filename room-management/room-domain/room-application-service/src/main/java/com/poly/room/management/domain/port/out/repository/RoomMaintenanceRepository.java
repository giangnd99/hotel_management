package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.RoomMaintenance;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
    
    // Issue type queries
    List<RoomMaintenance> findByIssueType(String issueType);
    List<RoomMaintenance> findByIssueTypeAndStatus(String issueType, String status);
    
    // Priority-based queries
    List<RoomMaintenance> findByPriority(String priority);
    List<RoomMaintenance> findByPriorityAndStatus(String priority, String status);
    
    // Date-based queries
    List<RoomMaintenance> findByRequestedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<RoomMaintenance> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<RoomMaintenance> findByCompletedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Staff assignment queries
    List<RoomMaintenance> findByAssignedTo(String staffId);
    List<RoomMaintenance> findByAssignedToAndStatus(String staffId, String status);
    List<RoomMaintenance> findByRequestedBy(String staffId);
    
    // Cost-based queries
    List<RoomMaintenance> findByEstimatedCostGreaterThan(BigDecimal cost);
    List<RoomMaintenance> findByActualCostBetween(BigDecimal minCost, BigDecimal maxCost);
    
    // Pagination
    List<RoomMaintenance> findAllWithPagination(int page, int size);
    
    // Statistics
    Long countByStatus(String status);
    Long countByIssueType(String issueType);
    Long countByPriority(String priority);
    Long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Active maintenance
    List<RoomMaintenance> findActiveMaintenance();
    List<RoomMaintenance> findOverdueMaintenance(LocalDateTime currentDate);
    
    // Urgent and critical maintenance
    List<RoomMaintenance> findUrgentMaintenance();
    List<RoomMaintenance> findCriticalMaintenance();
    List<RoomMaintenance> findByIsUrgent(Boolean isUrgent);
    
    // Cost statistics
    BigDecimal getTotalEstimatedCostByStatus(String status);
    BigDecimal getTotalActualCostByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<RoomMaintenance> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<RoomMaintenance> findByStaffId(String staffId);
}
