package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDto {
    private UUID checkInId;
    private UUID bookingId;
    private String guestName;
    private String guestPhone;
    private String guestEmail;
    private String roomNumber;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime checkInTime;
    private Integer numberOfGuests;
    private String specialRequests;
    private String status; // CHECKED_IN, EXTENDED, CHANGED_ROOM
    private String checkedInBy;
    private String notes;
}
