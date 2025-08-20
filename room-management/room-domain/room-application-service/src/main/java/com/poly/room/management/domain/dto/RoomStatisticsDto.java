package com.poly.room.management.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class RoomStatisticsDto {
    private Long totalRooms;
    private Long availableRooms;
    private Long occupiedRooms;
    private Long maintenanceRooms;
    private Long cleaningRooms;
    private Double occupancyRatio;
    private Double averageRoomPrice;
    private Long totalRevenue;
}