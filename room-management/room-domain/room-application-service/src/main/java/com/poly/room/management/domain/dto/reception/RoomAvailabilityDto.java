package com.poly.room.management.domain.dto.reception;

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
public class RoomAvailabilityDto {
    private UUID roomId;
    private String roomNumber;
    private String roomType;
    private Integer floor;
    private BigDecimal basePrice;
    private Boolean isAvailable;
}