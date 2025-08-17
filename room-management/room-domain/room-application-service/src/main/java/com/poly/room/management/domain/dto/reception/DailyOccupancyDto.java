package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyOccupancyDto {
    private LocalDate date;
    private Long totalRooms;
    private Long occupiedRooms;
    private Long availableRooms;
    private Long checkIns;
    private Long checkOuts;
    private Double occupancyRate;
    private BigDecimal revenue;
    private BigDecimal averageRoomRate;
    private Long totalGuests;
}
