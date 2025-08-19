package com.poly.room.management.dao.room.repository;

import com.poly.room.management.dao.room.entity.MaintenanceTypeEntity;
import com.poly.room.management.dao.room.entity.RoomMaintenanceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoomMaintenanceJpaRepository extends JpaRepository<RoomMaintenanceEntity, UUID> {

    // Room-specific queries
    List<RoomMaintenanceEntity> findByRoom_RoomId(UUID roomId);
    List<RoomMaintenanceEntity> findByRoom_RoomIdAndStatus(UUID roomId, String status);
    
    // Status-based queries
    List<RoomMaintenanceEntity> findByStatus(String status);
    Long countByStatus(String status);

    // Date-based queries
    List<RoomMaintenanceEntity> findByScheduledDateBetween(Timestamp startDate, Timestamp endDate);
    List<RoomMaintenanceEntity> findByCompletedDateBetween(Timestamp startDate, Timestamp endDate);
    
    // Maintenance type queries
    List<RoomMaintenanceEntity> findByMaintenanceType(MaintenanceTypeEntity maintenanceType);
    List<RoomMaintenanceEntity> findByMaintenanceTypeAndStatus(MaintenanceTypeEntity maintenanceType, String status);
    Long countByMaintenanceType(MaintenanceTypeEntity maintenanceType);

    // Priority-based queries
    List<RoomMaintenanceEntity> findByPriority(String priority);
    List<RoomMaintenanceEntity> findByPriorityAndStatus(String priority, String status);
    Long countByPriority(String priority);

    // Staff assignment queries
    List<RoomMaintenanceEntity> findByAssignedTo(String staffId);
    List<RoomMaintenanceEntity> findByAssignedToAndStatus(String staffId, String status);
    
    // Pagination queries
    Page<RoomMaintenanceEntity> findByStatus(String status, Pageable pageable);
    Page<RoomMaintenanceEntity> findByMaintenanceType(MaintenanceTypeEntity maintenanceType, Pageable pageable);
    Page<RoomMaintenanceEntity> findByPriority(String priority, Pageable pageable);
    
    // Active maintenance queries
    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.status IN ('REQUESTED', 'ASSIGNED', 'IN_PROGRESS')")
    List<RoomMaintenanceEntity> findActiveMaintenance();
    
    @Query("SELECT rm FROM RoomMaintenanceEntity rm WHERE rm.scheduledDate < :currentDate AND rm.status IN ('REQUESTED', 'ASSIGNED', 'IN_PROGRESS')")
    List<RoomMaintenanceEntity> findOverdueMaintenance(@Param("currentDate") LocalDateTime currentDate);
}
