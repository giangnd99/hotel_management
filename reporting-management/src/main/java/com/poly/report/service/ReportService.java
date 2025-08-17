package com.poly.report.service;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    // Filter by period of time
    List<Object> filterByPeriodOfTime(String period, String reportType);

    // Filter by date range
    List<Object> filterByDate(LocalDate fromDate, LocalDate toDate, String reportType);
}