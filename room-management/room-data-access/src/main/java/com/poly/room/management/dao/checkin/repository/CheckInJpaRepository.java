package com.poly.room.management.dao.checkin.repository;

import com.poly.room.management.dao.checkin.entity.CheckInEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CheckInJpaRepository extends JpaRepository<CheckInEntity, UUID> {

    // Basic CRUD operations
    List<CheckInEntity> findByRoomNumber(String roomNumber);
    List<CheckInEntity> findByGuestId(UUID guestId);
    List<CheckInEntity> findByBookingId(UUID bookingId);
    
    // Status-based queries
    List<CheckInEntity> findAllByStatus(CheckInEntity.CheckInStatus status);
    Page<CheckInEntity> findAllByStatus(CheckInEntity.CheckInStatus status, Pageable pageable);
    
    // Date-based queries
    List<CheckInEntity> findByCheckInDate(LocalDate checkInDate);
    List<CheckInEntity> findByCheckInDateBetween(LocalDate fromDate, LocalDate toDate);
    List<CheckInEntity> findByCheckOutDate(LocalDate checkOutDate);
    List<CheckInEntity> findByCheckOutDateBetween(LocalDate fromDate, LocalDate toDate);
    
    // Today's operations
    @Query("SELECT c FROM CheckInEntity c WHERE DATE(c.checkInDate) = :today")
    List<CheckInEntity> findTodayCheckIns(@Param("today") LocalDate today);
    
    @Query("SELECT c FROM CheckInEntity c WHERE DATE(c.checkOutDate) = :today")
    List<CheckInEntity> findTodayCheckOuts(@Param("today") LocalDate today);
    
    @Query("SELECT COUNT(c) FROM CheckInEntity c WHERE DATE(c.checkInDate) = :today")
    Long countTodayCheckIns(@Param("today") LocalDate today);
    
    @Query("SELECT COUNT(c) FROM CheckInEntity c WHERE DATE(c.checkOutDate) = :today")
    Long countTodayCheckOuts(@Param("today") LocalDate today);
    
    // Pending operations
    @Query("SELECT c FROM CheckInEntity c WHERE c.status = 'PENDING'")
    List<CheckInEntity> findPendingCheckIns();
    
    @Query("SELECT c FROM CheckInEntity c WHERE c.status = 'PENDING_CHECKOUT'")
    List<CheckInEntity> findPendingCheckOuts();
    
    // Current guests
    @Query("SELECT c FROM CheckInEntity c WHERE c.status IN ('CHECKED_IN', 'EXTENDED')")
    List<CheckInEntity> findCurrentGuests();
    
    @Query("SELECT c FROM CheckInEntity c WHERE c.roomNumber = :roomNumber AND c.status IN ('CHECKED_IN', 'EXTENDED')")
    List<CheckInEntity> findCurrentGuestsByRoomNumber(@Param("roomNumber") String roomNumber);
    
    // Staff operations
    List<CheckInEntity> findByCheckedInBy(String staffId);
    List<CheckInEntity> findByCheckedOutBy(String staffId);
    
    // Pagination
    Page<CheckInEntity> findAll(Pageable pageable);
    
    // Statistics
    @Query("SELECT COUNT(c) FROM CheckInEntity c WHERE c.status = :status")
    Long countByStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(c) FROM CheckInEntity c WHERE c.checkInDate BETWEEN :fromDate AND :toDate")
    Long countByDateRange(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);
    
    @Query("SELECT COUNT(c) FROM CheckInEntity c WHERE c.status IN ('CHECKED_IN', 'EXTENDED')")
    Long countCurrentGuests();
    
    // Room availability
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CheckInEntity c " +
           "WHERE c.roomNumber = :roomNumber AND c.status IN ('CHECKED_IN', 'EXTENDED')")
    Boolean isRoomOccupied(@Param("roomNumber") String roomNumber);
    
    @Query("SELECT c FROM CheckInEntity c WHERE c.roomNumber = :roomNumber AND c.status IN ('CHECKED_IN', 'EXTENDED')")
    List<CheckInEntity> findActiveCheckInByRoom(@Param("roomNumber") String roomNumber);
    
    // Extensions and changes
    @Query("SELECT c FROM CheckInEntity c WHERE c.status = 'EXTENDED'")
    List<CheckInEntity> findExtendedStays();
    
    @Query("SELECT c FROM CheckInEntity c WHERE c.status = 'CHANGED_ROOM'")
    List<CheckInEntity> findRoomChanges();
}
