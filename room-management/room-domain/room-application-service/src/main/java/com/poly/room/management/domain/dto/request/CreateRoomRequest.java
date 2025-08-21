package com.poly.room.management.domain.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    @NotBlank(message = "Room number is required")
    @Pattern(regexp = "^[0-9]{3,4}$", message = "Room number must be 3-4 digits")
    private String roomNumber;

    @NotNull(message = "Room type ID is required")
    private UUID roomTypeId;

    @NotNull(message = "Floor is required")
    @Min(value = 1, message = "Floor must be at least 1")
    @Max(value = 50, message = "Floor cannot exceed 50")
    private Integer floor;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal basePrice;

    private String description;
    private String amenities;
}
