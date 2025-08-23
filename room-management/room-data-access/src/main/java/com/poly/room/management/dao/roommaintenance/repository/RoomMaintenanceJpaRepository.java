package com.poly.room.management.dao.roommaintenance.repository;

import com.poly.room.management.dao.roommaintenance.entity.RoomMaintenanceEntity;
import com.poly.room.management.domain.entity.RoomMaintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoomMaintenanceJpaRepository extends JpaRepository<RoomMaintenanceEntity, UUID> {

    // Room-specific queries
    List<RoomMaintenanceEntity> findByRoom_RoomId(UUID roomId);

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.room.roomId = :roomId AND rm.status = :status")
    List<RoomMaintenanceEntity> findByRoomIdAndStatus(@Param("roomId") UUID roomId, @Param("status") String status);

    // Status-based queries
    List<RoomMaintenanceEntity> findByStatus(RoomMaintenanceEntity.MaintenanceStatus status);

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.status = :status")
    Page<RoomMaintenanceEntity> findByStatusWithPagination(@Param("status") String status, Pageable pageable);

    // Issue type queries
    List<RoomMaintenanceEntity> findByIssueType(RoomMaintenanceEntity.IssueType issueType);

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.issueType = :issueType AND rm.status = :status")
    List<RoomMaintenanceEntity> findByIssueTypeAndStatus(@Param("issueType") String issueType,
                                                         @Param("status") String status);

    // Priority-based queries
    List<RoomMaintenanceEntity> findByPriority(RoomMaintenanceEntity.MaintenancePriority priority);

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.priority = :priority AND rm.status = :status")
    List<RoomMaintenanceEntity> findByPriorityAndStatus(@Param("priority") String priority,
                                                        @Param("status") String status);

    // Date-based queries
    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.requestedAt BETWEEN :startDate AND :endDate")
    List<RoomMaintenanceEntity> findByRequestedDateBetween(@Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.scheduledAt BETWEEN :startDate AND :endDate")
    List<RoomMaintenanceEntity> findByScheduledDateBetween(@Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.completedAt BETWEEN :startDate AND :endDate")
    List<RoomMaintenanceEntity> findByCompletedDateBetween(@Param("startDate") LocalDateTime startDate,
                                                           @Param("endDate") LocalDateTime endDate);

    // Staff assignment queries
    List<RoomMaintenanceEntity> findByAssignedTo(String staffId);

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.assignedTo = :staffId AND rm.status = :status")
    List<RoomMaintenanceEntity> findByAssignedToAndStatus(@Param("staffId") String staffId,
                                                          @Param("status") String status);

    List<RoomMaintenanceEntity> findByRequestedBy(String staffId);

    // Cost-based queries
    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.estimatedCost > :cost")
    List<RoomMaintenanceEntity> findByEstimatedCostGreaterThan(@Param("cost") BigDecimal cost);

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.actualCost BETWEEN :minCost AND :maxCost")
    List<RoomMaintenanceEntity> findByActualCostBetween(@Param("minCost") BigDecimal minCost,
                                                        @Param("maxCost") BigDecimal maxCost);

    // Statistics
    @Query("SELECT COUNT(rm) FROM RoomMaintenanceEntity rm WHERE rm.status = :status")
    Long countByStatus(@Param("status") String status);

    @Query("SELECT COUNT(rm) FROM RoomMaintenanceEntity rm WHERE rm.issueType = :issueType")
    Long countByIssueType(@Param("issueType") String issueType);

    @Query("SELECT COUNT(rm) FROM RoomMaintenanceEntity rm WHERE rm.priority = :priority")
    Long countByPriority(@Param("priority") String priority);

    @Query("SELECT COUNT(rm) FROM RoomMaintenanceEntity rm WHERE rm.requestedAt BETWEEN :startDate AND :endDate")
    Long countByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Active maintenance
    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.status IN ('ASSIGNED', 'IN_PROGRESS')")
    List<RoomMaintenanceEntity> findActiveMaintenance();

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.scheduledAt < :currentDate AND rm.status != 'COMPLETED'")
    List<RoomMaintenanceEntity> findOverdueMaintenance(@Param("currentDate") LocalDateTime currentDate);

    // Urgent and critical maintenance
    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.priority = 'URGENT' OR rm.isUrgent = true")
    List<RoomMaintenanceEntity> findUrgentMaintenance();

    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.priority = 'CRITICAL'")
    List<RoomMaintenanceEntity> findCriticalMaintenance();

    List<RoomMaintenanceEntity> findByIsUrgent(Boolean isUrgent);

    // Cost statistics
    @Query("SELECT COALESCE(SUM(rm.estimatedCost), 0) FROM RoomMaintenanceEntity rm WHERE rm.status = :status")
    BigDecimal getTotalEstimatedCostByStatus(@Param("status") String status);

    @Query("SELECT COALESCE(SUM(rm.actualCost), 0) FROM RoomMaintenanceEntity rm WHERE rm.completedAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalActualCostByDateRange(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    List<RoomMaintenanceEntity> findAllByScheduledAtBetween(LocalDateTime scheduledAtAfter, LocalDateTime scheduledAtBefore);
}
