package com.poly.booking.management.dao.booking.repository;

import com.poly.booking.management.dao.booking.entity.BookingEntity;
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
public interface BookingJpaRepository extends JpaRepository<BookingEntity, UUID> {

    // Dashboard & Statistics
    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE DATE(b.checkIn) = :today")
    Long countTodayBookings(@Param("today") LocalDate today);
    
    @Query("SELECT COUNT(b) FROM BookingEntity b WHERE DATE(b.checkIn) = :today AND b.status = :status")
    Long countTodayBookingsByStatus(@Param("today") LocalDate today, @Param("status") String status);
    
    @Query("SELECT COALESCE(SUM(b.totalPrice), 0) FROM BookingEntity b WHERE DATE(b.checkIn) = :today")
    Double getTodayTotalRevenue(@Param("today") LocalDate today);
    
    @Query("SELECT COALESCE(AVG(b.totalPrice), 0) FROM BookingEntity b WHERE DATE(b.checkIn) = :today")
    Double getTodayAverageBookingValue(@Param("today") LocalDate today);
    
    // CRUD Operations
    Page<BookingEntity> findAll(Pageable pageable);
    
    // Search & Filter
    @Query("SELECT b FROM BookingEntity b WHERE " +
           "(:customerName IS NULL OR LOWER(b.customer.firstName) LIKE LOWER(CONCAT('%', :customerName, '%')) " +
           "OR LOWER(b.customer.lastName) LIKE LOWER(CONCAT('%', :customerName, '%'))) " +
           "AND (:customerEmail IS NULL OR LOWER(b.customer.email) LIKE LOWER(CONCAT('%', :customerEmail, '%'))) " +
           "AND (:roomNumber IS NULL OR EXISTS (SELECT br FROM BookingRoomEntity br WHERE br.booking = b AND br.room.roomNumber LIKE CONCAT('%', :roomNumber, '%'))) " +
           "AND (:checkInDate IS NULL OR DATE(b.checkIn) = :checkInDate) " +
           "AND (:checkOutDate IS NULL OR DATE(b.checkOut) = :checkOutDate)")
    Page<BookingEntity> searchBookings(@Param("customerName") String customerName,
                                       @Param("customerEmail") String customerEmail,
                                       @Param("roomNumber") String roomNumber,
                                       @Param("checkInDate") LocalDate checkInDate,
                                       @Param("checkOutDate") LocalDate checkOutDate,
                                       Pageable pageable);
    
    Page<BookingEntity> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT b FROM BookingEntity b WHERE DATE(b.checkIn) BETWEEN :fromDate AND :toDate")
    Page<BookingEntity> findByDateRange(@Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate,
                                        Pageable pageable);
    
    // Customer Specific
    Page<BookingEntity> findByCustomerId(UUID customerId, Pageable pageable);
    
    @Query("SELECT b FROM BookingEntity b WHERE b.customer.id = :customerId ORDER BY b.checkIn DESC")
    Page<BookingEntity> findCustomerBookingHistory(@Param("customerId") UUID customerId, Pageable pageable);
//
//    // Room Search
//    @Query("SELECT b FROM BookingEntity b WHERE " +
//           "(:checkInDate IS NULL OR DATE(b.checkIn) >= :checkInDate) " +
//           "AND (:checkOutDate IS NULL OR DATE(b.checkOut) <= :checkOutDate) " +
//           "AND (:numberOfGuests IS NULL OR EXISTS (SELECT br FROM BookingRoomEntity br WHERE br.booking = b AND br.room.capacity >= :numberOfGuests)) " +
//           "AND (:roomTypeId IS NULL OR EXISTS (SELECT br FROM BookingRoomEntity br WHERE br.booking = b AND br.room.roomType.id = :roomTypeId)) " +
//           "AND (:minPrice IS NULL OR b.totalPrice >= :minPrice) " +
//           "AND (:maxPrice IS NULL OR b.totalPrice <= :maxPrice)")
//    List<BookingEntity> findAvailableRooms(@Param("checkInDate") LocalDate checkInDate,
//                                           @Param("checkOutDate") LocalDate checkOutDate,
//                                           @Param("numberOfGuests") Integer numberOfGuests,
//                                           @Param("roomTypeId") Long roomTypeId,
//                                           @Param("minPrice") Integer minPrice,
//                                           @Param("maxPrice") Integer maxPrice);
}
