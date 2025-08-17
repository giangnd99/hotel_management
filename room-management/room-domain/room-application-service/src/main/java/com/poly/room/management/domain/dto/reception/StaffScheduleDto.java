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
public class StaffScheduleDto {
    private UUID scheduleId;
    private String staffId;
    private String staffName;
    private String department;
    private LocalDate date;
    private String shift; // MORNING, AFTERNOON, NIGHT
    private LocalTime startTime;
    private LocalTime endTime;
    private String status; // SCHEDULED, ON_DUTY, OFF_DUTY, ON_LEAVE
    private List<String> assignedTasks;
    private String supervisor;
    private String notes;
    private Boolean isOvertime;
    private Integer breakMinutes;
}
