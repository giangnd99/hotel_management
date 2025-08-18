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
public class DailyReportDto {
    private LocalDate reportDate;
    private Long totalRooms;
    private Long occupiedRooms;
    private Long availableRooms;
    private Long checkIns;
    private Long checkOuts;
    private Long walkInCheckIns;
    private Long earlyCheckOuts;
    private Long totalGuests;
    private Long newGuests;
    private Long returningGuests;
    private Double occupancyRate;
    private BigDecimal totalRevenue;
    private BigDecimal averageRoomRate;
    private BigDecimal totalServiceRevenue;
    private Long housekeepingRequests;
    private Long maintenanceRequests;
    private Long roomServiceRequests;
    private Long complaints;
    private Long compliments;
    private String notes;
}