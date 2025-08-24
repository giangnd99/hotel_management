package com.poly.booking.management.application.controller.rest;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.dto.request.UpdateBookingRequest;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.service.UpdateBookingService;
import com.poly.domain.valueobject.BookingId;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class UpdateBookingController {

    private final UpdateBookingService bookingService;

    @PutMapping("/{bookingId}")
    @Operation(summary = "Cập nhật booking")
    public ResponseEntity<Booking> updateBooking(@PathVariable UUID bookingId,
                                                 @RequestBody UpdateBookingRequest request) {
        if (bookingId == null || request == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        log.info("Updating booking: {}", bookingId);

        Booking updatedBooking = bookingService.updateBooking(bookingId, request);
        return ResponseEntity.ok(updatedBooking);
    }
}
