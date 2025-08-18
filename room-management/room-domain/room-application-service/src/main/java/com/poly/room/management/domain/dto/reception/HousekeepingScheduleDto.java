package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HousekeepingScheduleDto {
    private UUID scheduleId;
    private LocalDate date;
    private String shift; // MORNING, AFTERNOON, EVENING
    private LocalTime startTime;
    private LocalTime endTime;
    private List<HousekeepingAssignmentDto> assignments;
    private String supervisor;
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED
    private String notes;
}
