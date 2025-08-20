package com.poly.notification.management.dto.checkin;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequest {
    @NotNull(message = "Guest ID is required")
    private UUID guestId;

    @NotNull(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Check-in date is required")
    private LocalDateTime checkInTime;

    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 10, message = "Number of guests cannot exceed 10")
    private Integer numberOfGuests;

    private String specialRequests;
    private String notes;
    private String checkedInBy;
}