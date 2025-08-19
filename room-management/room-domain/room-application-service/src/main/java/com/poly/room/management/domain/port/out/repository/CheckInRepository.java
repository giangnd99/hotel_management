package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.CheckIn;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CheckInRepository {

    // Basic CRUD operations
    CheckIn save(CheckIn checkIn);
    Optional<CheckIn> findById(UUID id);
    List<CheckIn> findAll();
    void deleteById(UUID id);
    CheckIn update(CheckIn checkIn);

    // Check-in specific queries
    List<CheckIn> findByRoomNumber(String roomNumber);
    List<CheckIn> findByGuestId(UUID guestId);
    List<CheckIn> findByBookingId(UUID bookingId);
    
    // Status-based queries
    List<CheckIn> findByStatus(String status);
    List<CheckIn> findByStatusWithPagination(String status, int page, int size);
    
    // Date-based queries
    List<CheckIn> findByCheckInDate(LocalDate checkInDate);
    List<CheckIn> findByCheckInDateBetween(LocalDate fromDate, LocalDate toDate);
    List<CheckIn> findByCheckOutDate(LocalDate checkOutDate);
    List<CheckIn> findByCheckOutDateBetween(LocalDate fromDate, LocalDate toDate);
    
    // Today's operations
    List<CheckIn> findTodayCheckIns();
    List<CheckIn> findTodayCheckOuts();
    Long countTodayCheckIns();
    Long countTodayCheckOuts();
    
    // Pending operations
    List<CheckIn> findPendingCheckIns();
    List<CheckIn> findPendingCheckOuts();
    
    // Current guests
    List<CheckIn> findCurrentGuests();
    List<CheckIn> findCurrentGuestsByRoomNumber(String roomNumber);
    
    // Staff operations
    List<CheckIn> findByCheckedInBy(String staffId);
    List<CheckIn> findByCheckedOutBy(String staffId);
    
    // Pagination
    List<CheckIn> findAllWithPagination(int page, int size);
    
    // Statistics
    Long countByStatus(String status);
    Long countByDateRange(LocalDate fromDate, LocalDate toDate);
    Long countCurrentGuests();
    
    // Room availability
    Boolean isRoomOccupied(String roomNumber);
    Optional<CheckIn> findActiveCheckInByRoom(String roomNumber);
    
    // Extensions and changes
    List<CheckIn> findExtendedStays();
    List<CheckIn> findRoomChanges();
}
