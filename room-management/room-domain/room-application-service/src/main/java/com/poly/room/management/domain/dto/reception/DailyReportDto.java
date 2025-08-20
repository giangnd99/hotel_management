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
public class DailyReportDto {
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
    private Long newGuests;
    private Long returningGuests;
    private Long pendingCheckIns;
    private Long pendingCheckOuts;
    private Long maintenanceRequests;
    private Long cleaningRequests;
    private List<String> notes;
}