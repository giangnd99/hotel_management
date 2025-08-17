package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloorOccupancyDto {
    private Integer floor;
    private Long totalRooms;
    private Long occupiedRooms;
    private Double occupancyRate;
    private BigDecimal totalRevenue;
    private BigDecimal averageRate;
    private Long totalGuests;
    private String mostPopularRoomType;
}