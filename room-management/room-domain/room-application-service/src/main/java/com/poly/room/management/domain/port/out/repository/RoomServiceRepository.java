package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.RoomService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomServiceRepository {

    // Basic CRUD operations
    RoomService save(RoomService roomService);
    Optional<RoomService> findById(UUID id);
    List<RoomService> findAll();
    void deleteById(UUID id);
    RoomService update(RoomService roomService);

    // Room-specific queries
    List<RoomService> findByRoomNumber(String roomNumber);
    List<RoomService> findByRoomNumberAndStatus(String roomNumber, String status);
    
    // Guest-specific queries
    List<RoomService> findByGuestId(UUID guestId);
    List<RoomService> findByGuestIdAndStatus(UUID guestId, String status);
    
    // Service type queries
    List<RoomService> findByServiceType(String serviceType);
    List<RoomService> findByServiceTypeAndStatus(String serviceType, String status);
    
    // Status-based queries
    List<RoomService> findByStatus(String status);
    List<RoomService> findByStatusWithPagination(String status, int page, int size);
    
    // Date-based queries
    List<RoomService> findByRequestedDate(LocalDateTime requestedDate);
    List<RoomService> findByRequestedDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
    List<RoomService> findByCompletedDate(LocalDateTime completedDate);
    List<RoomService> findByCompletedDateBetween(LocalDateTime fromDate, LocalDateTime toDate);
    
    // Staff assignment queries
    List<RoomService> findByRequestedBy(String staffId);
    List<RoomService> findByCompletedBy(String staffId);
    List<RoomService> findByRequestedByAndStatus(String staffId, String status);
    
    // Pagination
    List<RoomService> findAllWithPagination(int page, int size);
    
    // Statistics
    Long countByStatus(String status);
    Long countByServiceType(String serviceType);
    Long countByRoomNumber(String roomNumber);
    Long countByDateRange(LocalDateTime fromDate, LocalDateTime toDate);
    
    // Active services
    List<RoomService> findActiveServices();
    List<RoomService> findActiveServicesByRoom(String roomNumber);
    
    // Pending services
    List<RoomService> findPendingServices();
    List<RoomService> findPendingServicesByRoom(String roomNumber);
    
    // Completed services
    List<RoomService> findCompletedServices();
    List<RoomService> findCompletedServicesByRoom(String roomNumber);
}
