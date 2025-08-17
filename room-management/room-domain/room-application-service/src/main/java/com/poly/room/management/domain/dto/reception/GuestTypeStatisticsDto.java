package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestTypeStatisticsDto {
    private String guestType; // INDIVIDUAL, GROUP, CORPORATE, VIP
    private Long totalGuests;
    private Long totalBookings;
    private Double averageLengthOfStay;
    private Double averageSpending;
    private Double repeatRate;
    private Long newGuests;
    private Long returningGuests;
}
