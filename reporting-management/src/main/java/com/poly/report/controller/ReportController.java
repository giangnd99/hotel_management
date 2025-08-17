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

    @GetMapping("/dashboard/available-room-count")
    @Operation(summary = "Lấy số phòng khả dụng", description = "Get available room count for dashboard")
    public ResponseEntity<Long> getAvailableRoomCount() {
        log.info("Getting available room count for dashboard");
        Long count = reportService.getAvailableRoomCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/dashboard/today-checkin-count")
    @Operation(summary = "Lấy số check-in hôm nay", description = "Get today check-in count for dashboard")
    public ResponseEntity<Long> getTodayCheckInCount() {
        log.info("Getting today check-in count for dashboard");
        Long count = reportService.getTodayCheckInCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/dashboard/today-checkout-count")
    @Operation(summary = "Lấy số check-out hôm nay", description = "Get today check-out count for dashboard")
    public ResponseEntity<Long> getTodayCheckOutCount() {
        log.info("Getting today check-out count for dashboard");
        Long count = reportService.getTodayCheckOutCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/dashboard/revenue-today")
    @Operation(summary = "Lấy doanh thu hôm nay", description = "Get today revenue for dashboard")
    public ResponseEntity<Double> getRevenueToday() {
        log.info("Getting today revenue for dashboard");
        Double revenue = reportService.getRevenueToday();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/dashboard/room-by-floor/{floorNumber}")
    @Operation(summary = "Lấy phòng theo tầng", description = "Get rooms by floor for dashboard")
    public ResponseEntity<List<Object>> getRoomByFloor(@PathVariable Integer floorNumber) {
        log.info("Getting rooms by floor: {} for dashboard", floorNumber);
        List<Object> rooms = reportService.getRoomByFloor(floorNumber);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/dashboard/room-info/{roomId}")
    @Operation(summary = "Lấy thông tin phòng theo ID", description = "Get room information by ID for dashboard")
    public ResponseEntity<Object> getRoomInfoById(@PathVariable Long roomId) {
        log.info("Getting room info by id: {} for dashboard", roomId);
        Object roomInfo = reportService.getRoomInfoById(roomId);
        return ResponseEntity.ok(roomInfo);
    }

    @PutMapping("/dashboard/room-status/{roomId}")
    @Operation(summary = "Cập nhật trạng thái phòng", description = "Update room status for dashboard")
    public ResponseEntity<Object> updateRoomStatus(
            @PathVariable Long roomId,
            @RequestParam String status) {
        log.info("Updating room status: {} to {} for dashboard", roomId, status);
        Object updatedRoom = reportService.updateRoomStatus(roomId, status);
        return ResponseEntity.ok(updatedRoom);
    }
}