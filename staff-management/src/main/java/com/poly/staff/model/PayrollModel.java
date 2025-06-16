package com.poly.staff.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PayrollModel {
    Integer payrollId;
    String staffId;
    Double totalSalary;
    YearMonth payrollMonth;
    LocalDateTime createdAt;
    LocalDate paymentDate;
}
