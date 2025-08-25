package com.poly.booking.management.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBookingRequest {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDate checkInDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 10, message = "Number of guests cannot exceed 10")
    private Integer numberOfGuests;
    @Nullable
    private String specialRequests;
    @Nullable
    private String notes;
}
