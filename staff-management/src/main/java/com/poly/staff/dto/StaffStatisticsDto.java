package com.poly.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffStatisticsDto {
    private Long totalStaff;
    private Long activeStaff;
    private Long inShiftStaff;
    private Long onLeaveStaff;
    private Long terminatedStaff;
    private Double averageSalary;
    private Long totalDepartments;
    private Long totalShifts;
}
