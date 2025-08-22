package com.poly.room.management.domain.dto.reception;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
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
    @Nullable
    private String bookingId;

    @NotNull(message = "Check-in date is required")
    private LocalDateTime checkInTime;

    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 10, message = "Number of guests cannot exceed 10")
    private Integer numberOfGuests;

    @Nullable
    private String specialRequests;
    @Nullable
    private String notes;
    @Nullable
    private String checkedInBy;
}