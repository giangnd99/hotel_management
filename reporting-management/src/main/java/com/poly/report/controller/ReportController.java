package com.poly.report.controller;

import com.poly.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Report Management", description = "APIs for report management")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/filter/period")
    @Operation(summary = "Filter reports by period of time", description = "Get reports filtered by time period")
    public ResponseEntity<List<Object>> filterByPeriodOfTime(
            @RequestParam String period, // DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY
            @RequestParam(required = false) String reportType) {
        log.info("Filtering reports by period: {}", period);
        List<Object> reports = reportService.filterByPeriodOfTime(period, reportType);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/filter/date")
    @Operation(summary = "Filter reports by date range", description = "Get reports filtered by from date to to date")
    public ResponseEntity<List<Object>> filterByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String reportType) {
        log.info("Filtering reports from {} to {}", fromDate, toDate);
        List<Object> reports = reportService.filterByDate(fromDate, toDate, reportType);
        return ResponseEntity.ok(reports);
    }
}