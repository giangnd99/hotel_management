package com.poly.booking.management.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "List Room ID is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<UUID> listRoomId;

    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 10, message = "Number of guests cannot exceed 10")
    private Integer numberOfGuests;

    @Nullable
    private String specialRequests;

    @Email(message = "Invalid email format")
    private String customerEmail;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number format")
    private String customerPhone;
}