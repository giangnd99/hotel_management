package com.poly.booking.management.domain.service;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.dto.BookingStatisticsDto;
import com.poly.booking.management.domain.dto.request.CreateBookingRequest;
import com.poly.booking.management.domain.dto.request.UpdateBookingRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingService {

    // Dashboard & Statistics
    BookingStatisticsDto getTodayBookingStatistics();
    Long getTodayBookingCount();
    Long getTodayBookingSuccessCount();
    Long getTodayBookingPendingCount();
    Long getTodayBookingCancelCount();

    // CRUD Operations
    List<BookingDto> getAllBookings(int page, int size);
    Optional<BookingDto> getBookingById(UUID bookingId);
    BookingDto createBooking(CreateBookingRequest request);
    BookingDto updateBooking(UUID bookingId, UpdateBookingRequest request);
    void deleteBooking(UUID bookingId);

    // Search & Filter
    List<BookingDto> searchBookings(String customerName, String customerEmail, String roomNumber,
                                    LocalDate checkInDate, LocalDate checkOutDate, int page, int size);
    List<BookingDto> filterBookingsByStatus(String status, int page, int size);
    List<BookingDto> filterBookingsByDateRange(LocalDate fromDate, LocalDate toDate, int page, int size);

    // Customer Specific
    List<BookingDto> getBookingsByCustomerId(UUID customerId, int page, int size);
    List<BookingDto> getCustomerBookingHistory(UUID customerId, int page, int size);

    // Room Search
    List<BookingDto> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate,
                                          Integer numberOfGuests, Long roomTypeId,
                                          Integer minPrice, Integer maxPrice);

    // Status Management
    BookingDto cancelBooking(UUID bookingId);
    BookingDto confirmBooking(UUID bookingId);
    BookingDto checkInBooking(UUID bookingId);
    BookingDto checkOutBooking(UUID bookingId);

    // Payment
    String getBookingPaymentStatus(UUID bookingId);
    BookingDto confirmBookingPayment(UUID bookingId);

    // Notifications
    void sendBookingConfirmation(UUID bookingId);
    void sendBookingReminder(UUID bookingId);
}
