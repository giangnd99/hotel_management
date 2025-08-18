package com.poly.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDto {
    private Long attendanceId;
    private String staffId;
    private String staffName;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String checkInLocation;
    private String checkOutLocation;
    private String status; // PRESENT, ABSENT, LATE, HALF_DAY
    private String notes;
}
