package com.poly.booking.management.domain.dto;

import com.poly.domain.valueobject.BookingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingDto {
    private UUID bookingId;
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private UUID roomId;
    private String roomNumber;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer numberOfGuests;
    private BigDecimal totalAmount;
    private String status; // PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
    private String paymentStatus; // PENDING, PAID, FAILED
    private String specialRequests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String notes;
}
