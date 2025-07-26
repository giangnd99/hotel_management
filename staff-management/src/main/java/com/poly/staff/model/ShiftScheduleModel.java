package com.poly.staff.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShiftScheduleModel {
    Integer scheduleId;
    String staffId;
    LocalDate workDate;
    LocalTime startTime;
    LocalTime endTime;
}
