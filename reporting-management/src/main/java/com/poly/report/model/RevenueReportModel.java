package com.poly.report.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RevenueReportModel {
    Integer departmentId;
    Double totalRevenue;
    Double totalCost;
    Double totalProfit;
    LocalDate reportDate;
}
