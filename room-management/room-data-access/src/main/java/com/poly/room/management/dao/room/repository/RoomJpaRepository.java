package com.poly.room.management.dao.room.repository;

import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.dao.room.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomJpaRepository extends JpaRepository<RoomEntity, UUID> {

    // Basic CRUD operations
    Optional<RoomEntity> findByRoomNumber(String roomNumber);
    boolean existsByRoomNumber(String roomNumber);

    // Room type queries
    List<RoomEntity> findByRoomType_RoomTypeId(Integer roomTypeId);
    Long countByRoomType_RoomTypeId(Integer roomTypeId);
    
    // Floor queries
    List<RoomEntity> findByFloor(Integer floor);
    Long countByFloor(Integer floor);
    @Query("SELECT DISTINCT r.floor FROM RoomEntity r ORDER BY r.floor")
    List<Integer> findAllDistinctFloors();
    
    // Pagination queries
    Page<RoomEntity> findByRoomType_RoomTypeId(Integer roomTypeId, Pageable pageable);
    Page<RoomEntity> findByFloor(Integer floor, Pageable pageable);
    
    // Advanced search with pagination
    @Query("SELECT r FROM RoomEntity r WHERE " +
           "(:roomNumber IS NULL OR r.roomNumber LIKE %:roomNumber%) AND " +
           "(:roomTypeId IS NULL OR r.roomType.roomTypeId = :roomTypeId) AND " +
           "(:status IS NULL OR r.roomStatus = :status) AND " +
           "(:floor IS NULL OR r.floor = :floor)")
    Page<RoomEntity> searchRooms(
            @Param("roomNumber") String roomNumber,
            @Param("roomTypeId") Integer roomTypeId,
            @Param("status") String status,
            @Param("floor") Integer floor,
            Pageable pageable
    );
    
    // Price range queries (assuming price is stored in RoomType)
    @Query("SELECT r FROM RoomEntity r JOIN r.roomType rt WHERE " +
           "(:minPrice IS NULL OR CAST(rt.basePrice AS double) >= :minPrice) AND " +
           "(:maxPrice IS NULL OR CAST(rt.basePrice AS double) <= :maxPrice)")
    Page<RoomEntity> findByPriceRange(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
    
    // Statistics queries
    @Query("SELECT MIN(CAST(rt.basePrice AS double)) FROM RoomEntity r JOIN r.roomType rt")
    Double findMinPrice();
    
    @Query("SELECT MAX(CAST(rt.basePrice AS double)) FROM RoomEntity r JOIN r.roomType rt")
    Double findMaxPrice();
    
    // Availability check
    @Query("SELECT CASE WHEN r.roomStatus = 'VACANT' THEN true ELSE false END FROM RoomEntity r WHERE r.roomId = :roomId")
    Boolean isRoomAvailable(@Param("roomId") UUID roomId);
    
    // Status-specific queries for statistics
    @Query("SELECT r FROM RoomEntity r WHERE r.roomStatus = 'VACANT'")
    Page<RoomEntity> findByStatusAvailable(Pageable pageable);
    
    @Query("SELECT r FROM RoomEntity r WHERE r.roomStatus = 'CHECKED_IN'")
    Page<RoomEntity> findByStatusOccupied(Pageable pageable);
    
    @Query("SELECT r FROM RoomEntity r WHERE r.roomStatus = 'MAINTENANCE'")
    Page<RoomEntity> findByStatusMaintenance(Pageable pageable);
    
    @Query("SELECT r FROM RoomEntity r WHERE r.roomStatus = 'CLEANING'")
    Page<RoomEntity> findByStatusCleaning(Pageable pageable);

    Page<RoomEntity> findAllByRoomStatus(RoomStatus roomStatus, Pageable pageable);

    Long countAllByRoomStatus(RoomStatus roomStatus);

}
