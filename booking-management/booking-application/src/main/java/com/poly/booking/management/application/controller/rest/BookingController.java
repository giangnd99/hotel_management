package com.poly.booking.management.application.controller.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

//    private final BookingManagementService bookingManagementService;
//
//    public BookingController(BookingManagementService bookingManagementService) {
//        this.bookingManagementService = bookingManagementService;
//    }
//
//    /**
//     * API to search for available rooms.
//     * GET /api/v1/bookings/rooms/search
//     * Example: /api/v1/bookings/rooms/search?checkInDate=2025-07-01&checkOutDate=2025-07-05&numberOfGuests=2
//     * /api/v1/bookings/rooms/search?checkInDate=2025-07-01&checkOutDate=2025-07-05&numberOfGuests=2&roomTypeId=1
//     */
//    @GetMapping("/rooms/search")
//    public ResponseEntity<List<RoomDto>> searchAvailableRooms(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
//            @RequestParam Integer numberOfGuests,
//            @RequestParam(required = false) Long roomTypeId) {
//        try {
//            RoomSearchQuery query = new RoomSearchQuery(checkInDate, checkOutDate, numberOfGuests, roomTypeId);
//            List<RoomDto> availableRooms = bookingManagementService.searchAvailableRooms(query);
//            return ResponseEntity.ok(availableRooms);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().build(); // Or return a more specific error DTO
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    /**
//     * API to create a new booking.
//     * POST /api/v1/bookings
//     */
//    @PostMapping
//    public ResponseEntity<BookingDto> createBooking(@RequestBody CreateBookingCommand command) {
//        try {
//            BookingDto newBooking = bookingManagementService.createBooking(command);
//            return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
//        } catch (IllegalArgumentException | IllegalStateException e) {
//            return ResponseEntity.badRequest().body(null); // Return error message
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    /**
//     * API to get booking details by ID.
//     * GET /api/v1/bookings/{bookingId}
//     */
//    @GetMapping("/{bookingId}")
//    public ResponseEntity<Object> getBookingById(@PathVariable UUID bookingId) {
//        return bookingManagementService.getBookingById(bookingId)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    /**
//     * API to get all bookings (for Admin/Staff).
//     * GET /api/v1/bookings
//     */
//    @GetMapping
//    public ResponseEntity<List<BookingDto>> getAllBookings() {
//        List<BookingDto> bookings = bookingManagementService.getAllBookings();
//        return ResponseEntity.ok(bookings);
//    }
//
//    /**
//     * API to get bookings by customer ID.
//     * GET /api/v1/bookings/customer/{customerId}
//     */
//    @GetMapping("/customer/{customerId}")
//    public ResponseEntity<List<BookingDto>> getBookingsByCustomerId(@PathVariable Long customerId) {
//        try {
//            List<BookingDto> bookings = bookingManagementService.getBookingsByCustomerId(customerId);
//            return ResponseEntity.ok(bookings);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    /**
//     * API to cancel a booking.
//     * PUT /api/v1/bookings/{bookingId}/cancel
//     */
//    @PutMapping("/{bookingId}/cancel")
//    public ResponseEntity<BookingDto> cancelBooking(@PathVariable UUID bookingId) {
//        try {
//            BookingDto cancelledBooking = bookingManagementService.cancelBooking(bookingId);
//            return ResponseEntity.ok(cancelledBooking);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build();
//        } catch (IllegalStateException e) {
//            return ResponseEntity.badRequest().body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    /**
//     * API to update booking status and details (for staff).
//     * PUT /api/v1/bookings/{bookingId}/status
//     * Example: { "status": "CHECKED_IN", "specialRequests": "Extra towels" }
//     */
//    @PutMapping("/{bookingId}/update")
//    public ResponseEntity<BookingDto> updateBooking(
//            @PathVariable UUID bookingId,
//            @RequestParam(required = false) EBookingStatus newStatus,
//            @RequestParam(required = false) String specialRequests,
//            @RequestParam(required = false) Integer numberOfGuests) {
//        try {
//            BookingDto updatedBooking = bookingManagementService.updateBookingDetails(bookingId, newStatus, specialRequests, numberOfGuests);
//            return ResponseEntity.ok(updatedBooking);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build();
//        } catch (IllegalStateException e) {
//            return ResponseEntity.badRequest().body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    // Additional CRUD operations for admin/staff can be added, e.g.,
//    // DELETE /api/v1/bookings/{bookingId}
//    // @DeleteMapping("/{bookingId}")
//    // public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
//    //     try {
//    //         bookingManagementService.deleteBooking(bookingId); // Need to implement this in service
//    //         return ResponseEntity.noContent().build();
//    //     } catch (IllegalArgumentException e) {
//    //         return ResponseEntity.notFound().build();
//    //     }
//    // }
}
