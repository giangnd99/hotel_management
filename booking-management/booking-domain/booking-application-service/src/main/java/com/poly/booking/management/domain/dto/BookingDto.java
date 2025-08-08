package com.poly.booking.management.domain.dto;

import com.poly.domain.valueobject.EBookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingDto {
    private UUID bookingId;
    private Long customerId;
    private String customerName; // Added for display
    private Long roomId;
    private String roomNumber; // Added for display
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private BigDecimal totalAmount;
    private LocalDateTime bookingDate;
    private EBookingStatus status;
    private String specialRequests;
}
