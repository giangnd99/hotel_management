package com.poly.booking.management.application.controller.rest;

import com.poly.booking.management.domain.dto.*;
import com.poly.booking.management.domain.dto.request.CreateBookingRequest;
import com.poly.booking.management.domain.dto.request.UpdateBookingRequest;
import com.poly.booking.management.domain.dto.response.DepositBookingResponse;
import com.poly.booking.management.domain.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Controller", description = "Quản lý đặt phòng")
@Slf4j(topic = "BOOKING-CONTROLLER")
public class BookingController {

    private final BookingService bookingService;

    // ========== DASHBOARD & STATISTICS APIs ==========

    @GetMapping("/statistics/today")
    @Operation(summary = "Lấy thống kê booking hôm nay")
    public ResponseEntity<BookingStatisticsDto> getTodayBookingStatistics() {
        log.info("Getting today booking statistics");
        BookingStatisticsDto statistics = bookingService.getTodayBookingStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/count/today")
    @Operation(summary = "Lấy tổng số booking hôm nay")
    public ResponseEntity<Integer> getTodayBookingCount() {
        log.info("Getting today booking count");
        Integer count = bookingService.getTodayBookingCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/today/success")
    @Operation(summary = "Lấy số booking thành công hôm nay (gửi mail thành công)")
    public ResponseEntity<Integer> getTodayBookingSuccessCount() {
        log.info("Getting today successful booking count");
        Integer count = bookingService.getTodayBookingSuccessCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/today/pending")
    @Operation(summary = "Lấy số booking đang chờ hôm nay (đang gửi mail/thanh toán)")
    public ResponseEntity<Integer> getTodayBookingPendingCount() {
        log.info("Getting today pending booking count");
        Integer count = bookingService.getTodayBookingPendingCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/today/cancelled")
    @Operation(summary = "Lấy số booking đã hủy hôm nay")
    public ResponseEntity<Integer> getTodayBookingCancelCount() {
        log.info("Getting today cancelled booking count");
        Integer count = bookingService.getTodayBookingCancelCount();
        return ResponseEntity.ok(count);
    }

    // ========== CRUD APIs ==========

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả booking")
    public ResponseEntity<List<BookingDto>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting all bookings with page: {}, size: {}", page, size);
        List<BookingDto> bookings = bookingService.getAllBookings(page, size);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Lấy booking theo ID")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable UUID bookingId) {
        log.info("Getting booking by id: {}", bookingId);
        return bookingService.getBookingById(bookingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Tạo booking mới")
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        log.info("Creating new booking for customer: {}", request.getCustomerId());
        BookingDto newBooking = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
    }

    @PutMapping("/{bookingId}")
    @Operation(summary = "Cập nhật booking")
    public ResponseEntity<BookingDto> updateBooking(
            @PathVariable UUID bookingId,
            @Valid @RequestBody UpdateBookingRequest request) {
        log.info("Updating booking: {}", bookingId);
        BookingDto updatedBooking = bookingService.updateBooking(bookingId, request);
        return ResponseEntity.ok(updatedBooking);
    }

    @DeleteMapping("/{bookingId}")
    @Operation(summary = "Xóa booking")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        log.info("Deleting booking: {}", bookingId);
        bookingService.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    // ========== SEARCH & FILTER APIs ==========

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm booking")
    public ResponseEntity<List<BookingDto>> searchBookings(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID roomId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Searching bookings with filters");
        List<BookingDto> bookings = bookingService.searchBookings(
                customerId, roomId, checkInDate, checkOutDate, page, size);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/filter/status/{status}")
    @Operation(summary = "Lọc booking theo trạng thái")
    public ResponseEntity<List<BookingDto>> filterBookingsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering bookings by status: {}", status);
        List<BookingDto> bookings = bookingService.filterBookingsByStatus(status, page, size);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/filter/date-range")
    @Operation(summary = "Lọc booking theo khoảng thời gian")
    public ResponseEntity<List<BookingDto>> filterBookingsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering bookings by date range: {} to {}", fromDate, toDate);
        List<BookingDto> bookings = bookingService.filterBookingsByDateRange(fromDate, toDate, page, size);
        return ResponseEntity.ok(bookings);
    }

    // ========== CUSTOMER SPECIFIC APIs ==========

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Lấy booking theo customer ID")
    public ResponseEntity<List<BookingDto>> getBookingsByCustomerId(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting bookings for customer: {}", customerId);
        List<BookingDto> bookings = bookingService.getBookingsByCustomerId(customerId, page, size);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/customer/{customerId}/history")
    @Operation(summary = "Lấy lịch sử booking của customer")
    public ResponseEntity<List<BookingDto>> getCustomerBookingHistory(
            @PathVariable UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting booking history for customer: {}", customerId);
        List<BookingDto> bookings = bookingService.getCustomerBookingHistory(customerId, page, size);
        return ResponseEntity.ok(bookings);
    }

    // ========== ROOM SEARCH APIs ==========
//
//    @GetMapping("/rooms/search")
//    @Operation(summary = "Tìm kiếm phòng khả dụng")
//    public ResponseEntity<List<BookingDto>> searchAvailableRooms(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
//            @RequestParam Integer numberOfGuests,
//            @RequestParam(required = false) Long roomTypeId,
//            @RequestParam(required = false) Integer minPrice,
//            @RequestParam(required = false) Integer maxPrice) {
//        log.info("Searching available rooms for dates: {} to {}, guests: {}",
//                checkInDate, checkOutDate, numberOfGuests);
//        List<BookingDto> availableRooms = bookingService.searchAvailableRooms(
//                checkInDate, checkOutDate, numberOfGuests, roomTypeId, minPrice, maxPrice);
//        return ResponseEntity.ok(availableRooms);
//    }

    // ========== BOOKING STATUS MANAGEMENT ==========

    @PutMapping("/{bookingId}/cancel")
    @Operation(summary = "Hủy booking")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable UUID bookingId) {
        log.info("Cancelling booking: {}", bookingId);
        BookingDto cancelledBooking = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(cancelledBooking);
    }

    @PutMapping("/{bookingId}/confirm")
    @Operation(summary = "Xác nhận booking")
    public ResponseEntity<BookingDto> confirmBooking(@PathVariable UUID bookingId) {
        log.info("Confirming booking: {}", bookingId);
        BookingDto confirmedBooking = bookingService.confirmBooking(bookingId);
        return ResponseEntity.ok(confirmedBooking);
    }

    @PutMapping("/{bookingId}/check-in")
    @Operation(summary = "Check-in booking")
    public ResponseEntity<List<UUID>> checkInBooking(@PathVariable UUID bookingId) {
        log.info("Checking in booking: {}", bookingId);
        List<UUID> checkedInBooking = bookingService.checkInBooking(bookingId);
        return ResponseEntity.ok(checkedInBooking);
    }

    @PutMapping("/{bookingId}/check-out")
    @Operation(summary = "Check-out booking")
    public ResponseEntity<List<UUID>> checkOutBooking(@PathVariable UUID bookingId) {
        log.info("Checking out booking: {}", bookingId);
        List<UUID> checkedOutBooking = bookingService.checkOutBooking(bookingId);
        return ResponseEntity.ok(checkedOutBooking);
    }

    // ========== PAYMENT RELATED APIs ==========

    @GetMapping("/{bookingId}/payment-status")
    @Operation(summary = "Lấy trạng thái thanh toán của booking")
    public ResponseEntity<String> getBookingPaymentStatus(@PathVariable UUID bookingId) {
        log.info("Getting payment status for booking: {}", bookingId);
        String paymentStatus = bookingService.getBookingPaymentStatus(bookingId);
        return ResponseEntity.ok(paymentStatus);
    }

    @PutMapping("/{bookingId}/payment/confirm")
    @Operation(summary = "Xác nhận thanh toán booking")
    public ResponseEntity<DepositBookingResponse> confirmBookingPayment(@PathVariable UUID bookingId) {
        log.info("Confirming payment for booking: {}", bookingId);
        DepositBookingResponse updatedBooking = bookingService.confirmBookingPayment(bookingId);
        return ResponseEntity.ok(updatedBooking);
    }

    // ========== NOTIFICATION APIs ==========

    @PostMapping("/{bookingId}/send-confirmation")
    @Operation(summary = "Gửi email xác nhận booking")
    public ResponseEntity<Void> sendBookingConfirmation(@PathVariable UUID bookingId) {
        log.info("Sending confirmation email for booking: {}", bookingId);
        bookingService.sendBookingConfirmation(bookingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bookingId}/send-reminder")
    @Operation(summary = "Gửi email nhắc nhở booking")
    public ResponseEntity<Void> sendBookingReminder(@PathVariable UUID bookingId) {
        log.info("Sending reminder email for booking: {}", bookingId);
        bookingService.sendBookingReminder(bookingId);
        return ResponseEntity.ok().build();
    }
}
