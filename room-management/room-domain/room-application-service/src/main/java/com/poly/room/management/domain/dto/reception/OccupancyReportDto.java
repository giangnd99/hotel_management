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
    private Double averageOccupancyRate;
    private Double peakOccupancyRate;
    private LocalDate peakDate;
    private Double lowestOccupancyRate;
    private LocalDate lowestDate;
    private BigDecimal totalRevenue;
    private BigDecimal averageDailyRevenue;
    private BigDecimal revenuePerRoom;
    private List<DailyOccupancyDto> dailyData;
    private List<RoomTypeOccupancyDto> roomTypeData;
    private List<FloorOccupancyDto> floorData;
}
