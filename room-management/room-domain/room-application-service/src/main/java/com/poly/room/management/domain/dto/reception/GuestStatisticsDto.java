package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuestStatisticsDto {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long totalGuests;
    private Long newGuests;
    private Long returningGuests;
    private Long walkInGuests;
    private Long groupGuests;
    private Long corporateGuests;
    private Long individualGuests;
    private Double averageLengthOfStay;
    private Integer maxLengthOfStay;
    private Integer minLengthOfStay;
    private List<GuestTypeStatisticsDto> guestTypeBreakdown;
    private List<NationalityStatisticsDto> nationalityBreakdown;
    private List<DailyGuestDto> dailyGuestData;
    private List<GuestSatisfactionDto> satisfactionData;
}
