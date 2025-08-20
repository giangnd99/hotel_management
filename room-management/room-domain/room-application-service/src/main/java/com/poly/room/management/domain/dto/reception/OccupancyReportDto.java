package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccupancyReportDto {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long totalRooms;
    private Long totalNights;
    private Long occupiedNights;
    private Long availableNights;
    private Double occupancyRate;
    private BigDecimal totalRevenue;
    private BigDecimal averageRoomRate;
    private Long totalCheckIns;
    private Long totalCheckOuts;
    private Long totalGuests;
    private List<DailyOccupancyDto> dailyBreakdown;
    private String notes;
}
