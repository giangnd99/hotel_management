package com.poly.booking.management.domain.port.out.repository;

import com.poly.booking.management.domain.entity.Booking;
import com.poly.domain.valueobject.BookingId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(BookingId bookingId);
    
    // Dashboard & Statistics
    Long countTodayBookings();
    Long countTodayBookingsByStatus(String status);
    Double getTodayTotalRevenue();
    Double getTodayAverageBookingValue();
    
    // CRUD Operations
    List<Booking> findAll(int page, int size);
    void deleteById(BookingId bookingId);
    
    // Search & Filter
    List<Booking> searchBookings(String customerName, String customerEmail, String roomNumber,
                                LocalDate checkInDate, LocalDate checkOutDate, int page, int size);
    List<Booking> filterBookingsByStatus(String status, int page, int size);
    List<Booking> filterBookingsByDateRange(LocalDate fromDate, LocalDate toDate, int page, int size);
    
    // Customer Specific
    List<Booking> findBookingsByCustomerId(UUID customerId, int page, int size);
    List<Booking> findCustomerBookingHistory(UUID customerId, int page, int size);
    
//    // Room Search
//    List<Booking> findAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate,
//                                    Integer numberOfGuests, Long roomTypeId,
//                                    Integer minPrice, Integer maxPrice);
}
