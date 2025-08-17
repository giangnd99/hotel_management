package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionDashboardDto {
    private Long availableRooms;
    private Long occupiedRooms;
    private Long todayCheckIns;
    private Long todayCheckOuts;
    private Long todayCheckInGuests;
    private Long todayCheckOutGuests;
    private Long pendingCheckIns;
    private Long pendingCheckOuts;
    private Long currentGuests;
    private Double occupancyRate;
    private Long pendingHousekeeping;
    private Long pendingMaintenance;
}
