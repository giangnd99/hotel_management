package com.poly.booking.management.application.controller.rest;

import com.poly.booking.management.domain.dto.BookingDto;
import com.poly.booking.management.domain.dto.response.BookingCreatedResponse;
import com.poly.booking.management.domain.entity.Booking;
import com.poly.booking.management.domain.entity.BookingRoom;
import com.poly.booking.management.domain.service.impl.BookingCancellationDomainService;
import com.poly.domain.valueobject.BookingStatus;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class CancelBookingController {

    private final BookingCancellationDomainService bookingCancellationDomainService;

    @PostMapping("/{bookingId}/cancel")
    @Operation(summary = "Hủy booking")
    public ResponseEntity<BookingCreatedResponse> cancelBooking(@PathVariable UUID bookingId,
                                                                @RequestParam(required = false) String reason) {
        log.info("Cancelling booking: {}", bookingId);
        if (bookingId == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        Booking cancelledBooking = bookingCancellationDomainService.cancelBooking(bookingId, reason);
        BookingCreatedResponse response = BookingCreatedResponse.builder()
                .customerId(cancelledBooking.getCustomer().getId().getValue())
                .bookingId(cancelledBooking.getId().getValue())
                .bookingDate(cancelledBooking.getCheckInDate().getValue())
                .numberOfGuests(cancelledBooking.getNumberOfGuests())
                .checkInDate(cancelledBooking.getCheckInDate().getValue())
                .checkOutDate(cancelledBooking.getCheckOutDate().getValue())
                .status(BookingStatus.CANCELLED)
                .totalAmount(cancelledBooking.getTotalPrice().getAmount())
                .rooms(cancelledBooking.getBookingRooms().stream().map(BookingRoom::getRoom)
                        .toList())
                .build();
        return ResponseEntity.ok(response);
    }
}
