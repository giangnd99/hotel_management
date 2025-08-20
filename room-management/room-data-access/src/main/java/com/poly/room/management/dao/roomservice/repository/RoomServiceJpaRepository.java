package com.poly.room.management.dao.roomservice.repository;

import com.poly.room.management.dao.roomservice.entity.RoomServiceEntity;
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
public interface RoomServiceJpaRepository extends JpaRepository<RoomServiceEntity, UUID> {

    // Basic CRUD operations
    // Room-specific queries
    List<RoomServiceEntity> findByRoomNumber(String roomNumber);
    List<RoomServiceEntity> findByRoomNumberAndStatus(String roomNumber, RoomServiceEntity.ServiceStatus status);
    
    // Guest-specific queries
    List<RoomServiceEntity> findByGuestId(UUID guestId);
    List<RoomServiceEntity> findByGuestIdAndStatus(UUID guestId, RoomServiceEntity.ServiceStatus status);
    
    // Service type queries
    List<RoomServiceEntity> findByServiceType(String serviceType);
    List<RoomServiceEntity> findByServiceTypeAndStatus(String serviceType, RoomServiceEntity.ServiceStatus status);
    
    // Status-based queries
    List<RoomServiceEntity> findByStatus(RoomServiceEntity.ServiceStatus status);
    Page<RoomServiceEntity> findByStatus(RoomServiceEntity.ServiceStatus status, Pageable pageable);
    
    // Date-based queries
    List<RoomServiceEntity> findAllByCreatedAt(LocalDateTime requestedDate);
    List<RoomServiceEntity> findAllByRequestedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);

    List<RoomServiceEntity> findAllByCompletedAt(LocalDateTime completedAt);
    List<RoomServiceEntity> findAllByCompletedAtBetween(LocalDateTime fromDate, LocalDateTime toDate);
    
    // Staff assignment queries
    List<RoomServiceEntity> findByRequestedBy(String staffId);
    List<RoomServiceEntity> findByCompletedBy(String staffId);

    List<RoomServiceEntity> findAllByRequestedByAndStatus(String requestedBy, RoomServiceEntity.ServiceStatus status);

    // Statistics
    @Query("SELECT COUNT(rs) FROM RoomServiceEntity rs WHERE rs.status = :status")
    Long countByStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(rs) FROM RoomServiceEntity rs WHERE rs.serviceType = :serviceType")
    Long countByServiceType(@Param("serviceType") String serviceType);
    
    @Query("SELECT COUNT(rs) FROM RoomServiceEntity rs WHERE rs.roomNumber = :roomNumber")
    Long countByRoomNumber(@Param("roomNumber") String roomNumber);
    
    @Query("SELECT COUNT(rs) FROM RoomServiceEntity rs WHERE rs.requestedAt BETWEEN :fromDate AND :toDate")
    Long countByDateRange(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
    
    // Active services
    @Query("SELECT rs FROM RoomServiceEntity rs WHERE rs.status IN ('REQUESTED', 'IN_PROGRESS')")
    List<RoomServiceEntity> findActiveServices();
    
    @Query("SELECT rs FROM RoomServiceEntity rs WHERE rs.roomNumber = :roomNumber AND rs.status IN ('REQUESTED', 'IN_PROGRESS')")
    List<RoomServiceEntity> findActiveServicesByRoom(@Param("roomNumber") String roomNumber);
    
    // Pending services
    @Query("SELECT rs FROM RoomServiceEntity rs WHERE rs.status = 'REQUESTED'")
    List<RoomServiceEntity> findPendingServices();
    
    @Query("SELECT rs FROM RoomServiceEntity rs WHERE rs.roomNumber = :roomNumber AND rs.status = 'REQUESTED'")
    List<RoomServiceEntity> findPendingServicesByRoom(@Param("roomNumber") String roomNumber);
    
    // Completed services
    @Query("SELECT rs FROM RoomServiceEntity rs WHERE rs.status = 'COMPLETED'")
    List<RoomServiceEntity> findCompletedServices();
    
    @Query("SELECT rs FROM RoomServiceEntity rs WHERE rs.roomNumber = :roomNumber AND rs.status = 'COMPLETED'")
    List<RoomServiceEntity> findCompletedServicesByRoom(@Param("roomNumber") String roomNumber);
}
