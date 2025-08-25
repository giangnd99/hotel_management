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
    private String roomNumber;

    private UUID roomTypeId;

    private Integer floor;

    private BigDecimal basePrice;

    private String description;
    private String area;
}
