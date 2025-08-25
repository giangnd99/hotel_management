package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    private Long totalCheckIns;
    private Long totalCheckOuts;
    private Long totalNights;
    private Double averageStayDuration;
    private Map<String, Long> guestsByNationality;
    private Map<String, Long> guestsByIdType;
    private Map<String, Long> guestsByGender;
    private List<String> topNationalities;
    private List<String> notes;
}
