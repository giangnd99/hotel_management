package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NationalityStatisticsDto {
    private String nationality;
    private Long totalGuests;
    private Long totalBookings;
    private Double averageLengthOfStay;
    private Double averageSpending;
    private String preferredRoomType;
    private String preferredSeason;
}