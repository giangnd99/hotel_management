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
public class UpdateRoomRequest {

    private String roomId;

    private UUID roomTypeId;

    @Min(value = 1, message = "Floor must be at least 1")
    @Max(value = 50, message = "Floor cannot exceed 50")
    private Integer floor;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal basePrice;

    private String description;
    private String amenities;
    private String status;
}
