package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInPendingDto {
    private UUID bookingId;
    private String guestName;
    private String guestPhone;
    private String guestEmail;
    private String roomNumber;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime expectedCheckInTime;
    private Integer numberOfGuests;
    private String specialRequests;
    private String status; // CONFIRMED, PENDING_PAYMENT
}