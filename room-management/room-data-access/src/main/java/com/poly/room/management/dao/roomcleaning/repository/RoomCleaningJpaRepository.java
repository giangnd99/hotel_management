package com.poly.room.management.dao.roomcleaning.repository;

import com.poly.room.management.dao.roomcleaning.entity.RoomCleaningEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface RoomCleaningJpaRepository extends JpaRepository<RoomCleaningEntity, UUID> {

    // Room-specific queries
    List<RoomCleaningEntity> findByRoomId(UUID roomId);
    
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.roomId = :roomId AND rc.status = :status")
    List<RoomCleaningEntity> findByRoomIdAndStatus(@Param("roomId") UUID roomId, @Param("status") String status);
    
    // Status-based queries
    List<RoomCleaningEntity> findByStatus(RoomCleaningEntity.CleaningStatus status);
    
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.status = :status")
    Page<RoomCleaningEntity> findByStatusWithPagination(@Param("status") String status, Pageable pageable);
    
    // Date-based queries
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.scheduledAt BETWEEN :startDate AND :endDate")
    List<RoomCleaningEntity> findByScheduledDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.completedAt BETWEEN :startDate AND :endDate")
    List<RoomCleaningEntity> findByCompletedDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.requestedAt BETWEEN :startDate AND :endDate")
    List<RoomCleaningEntity> findByRequestedDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                       @Param("endDate") LocalDateTime endDate);
    
    // Cleaning type queries
    List<RoomCleaningEntity> findByCleaningType(RoomCleaningEntity.CleaningType cleaningType);
    
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.cleaningType = :cleaningType AND rc.status = :status")
    List<RoomCleaningEntity> findByCleaningTypeAndStatus(@Param("cleaningType") String cleaningType, 
                                                        @Param("status") String status);
    
    // Priority-based queries
    List<RoomCleaningEntity> findByPriority(RoomCleaningEntity.CleaningPriority priority);
    
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.priority = :priority AND rc.status = :status")
    List<RoomCleaningEntity> findByPriorityAndStatus(@Param("priority") String priority, 
                                                    @Param("status") String status);
    
    // Staff assignment queries
    List<RoomCleaningEntity> findByAssignedTo(String staffId);
    
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.assignedTo = :staffId AND rc.status = :status")
    List<RoomCleaningEntity> findByAssignedToAndStatus(@Param("staffId") String staffId, 
                                                      @Param("status") String status);
    
    List<RoomCleaningEntity> findByRequestedBy(String staffId);
    
    // Statistics
    @Query("SELECT COUNT(rc) FROM RoomCleaningEntity rc WHERE rc.status = :status")
    Long countByStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(rc) FROM RoomCleaningEntity rc WHERE rc.cleaningType = :cleaningType")
    Long countByCleaningType(@Param("cleaningType") String cleaningType);
    
    @Query("SELECT COUNT(rc) FROM RoomCleaningEntity rc WHERE rc.priority = :priority")
    Long countByPriority(@Param("priority") String priority);
    
    // Active cleaning
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.status IN ('ASSIGNED', 'IN_PROGRESS')")
    List<RoomCleaningEntity> findActiveCleaning();
    
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.scheduledAt < :currentDate AND rc.status != 'COMPLETED'")
    List<RoomCleaningEntity> findOverdueCleaning(@Param("currentDate") LocalDateTime currentDate);
    
    // Urgent cleaning
    @Query("SELECT rc FROM RoomCleaningEntity rc WHERE rc.priority = 'URGENT' OR rc.isUrgent = true")
    List<RoomCleaningEntity> findUrgentCleaning();
    
    List<RoomCleaningEntity> findByIsUrgent(Boolean isUrgent);
}
