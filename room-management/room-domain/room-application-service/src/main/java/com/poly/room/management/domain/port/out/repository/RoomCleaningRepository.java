package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.RoomCleaning;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomCleaningRepository {

    // Basic CRUD operations
    RoomCleaning save(RoomCleaning roomCleaning);
    Optional<RoomCleaning> findById(UUID id);
    List<RoomCleaning> findAll();
    void deleteById(UUID id);
    RoomCleaning update(RoomCleaning roomCleaning);

    // Room-specific queries
    List<RoomCleaning> findByRoomId(UUID roomId);
    List<RoomCleaning> findByRoomIdAndStatus(UUID roomId, String status);
    
    // Status-based queries
    List<RoomCleaning> findByStatus(String status);
    List<RoomCleaning> findByStatusWithPagination(String status, int page, int size);
    
    // Date-based queries
    List<RoomCleaning> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<RoomCleaning> findByCompletedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<RoomCleaning> findByRequestedDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Cleaning type queries
    List<RoomCleaning> findByCleaningType(String cleaningType);
    List<RoomCleaning> findByCleaningTypeAndStatus(String cleaningType, String status);
    
    // Priority-based queries
    List<RoomCleaning> findByPriority(String priority);
    List<RoomCleaning> findByPriorityAndStatus(String priority, String status);
    
    // Staff assignment queries
    List<RoomCleaning> findByAssignedTo(String staffId);
    List<RoomCleaning> findByAssignedToAndStatus(String staffId, String status);
    List<RoomCleaning> findByRequestedBy(String staffId);
    
    // Pagination
    List<RoomCleaning> findAllWithPagination(int page, int size);
    
    // Statistics
    Long countByStatus(String status);
    Long countByCleaningType(String cleaningType);
    Long countByPriority(String priority);
    
    // Active cleaning
    List<RoomCleaning> findActiveCleaning();
    List<RoomCleaning> findOverdueCleaning(LocalDateTime currentDate);
    
    // Urgent cleaning
    List<RoomCleaning> findUrgentCleaning();
    List<RoomCleaning> findByIsUrgent(Boolean isUrgent);
}
