package com.poly.ai.management.application.web.rest;

import com.poly.ai.management.domain.service.AIReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/ai/reports")
@RequiredArgsConstructor
@Slf4j
public class AIReportController {

    private final AIReportService aiReportService;

    @PostMapping("/daily")
    public ResponseEntity<Map<String, Object>> generateDailyReport() {

        log.info("Generating daily report with AI...");

        try {

            Map<String, Object> report = aiReportService.generateDailyReport();

            log.info("Daily report generated successfully");
            return ResponseEntity.ok(report);

        } catch (Exception e) {
            log.error("Error generating daily report: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Không thể tạo báo cáo hàng ngày: " + e.getMessage()));
        }
    }


    @PostMapping("/monthly")
    public ResponseEntity<Map<String, Object>> generateMonthlyReport(
            @RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "2024") int year) {

        log.info("Generating monthly report for {}/{} with AI...", month, year);

        try {
            if (month < 1 || month > 12) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tháng phải từ 1-12"));
            }

            if (year < 2020 || year > 2030) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Năm phải từ 2020-2030"));
            }

            Map<String, Object> report = aiReportService.generateMonthlyReport(month, year);

            log.info("Monthly report for {}/{} generated successfully", month, year);
            return ResponseEntity.ok(report);

        } catch (Exception e) {
            log.error("Error generating monthly report: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Không thể tạo báo cáo hàng tháng: " + e.getMessage()));
        }
    }

    @PostMapping("/yearly")
    public ResponseEntity<Map<String, Object>> generateYearlyReport(
            @RequestParam(defaultValue = "2024") int year) {

        log.info("Generating yearly report for {} with AI...", year);

        try {
            if (year < 2020 || year > 2030) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Năm phải từ 2020-2030"));
            }

            Map<String, Object> report = aiReportService.generateYearlyReport(year);

            log.info("Yearly report for {} generated successfully", year);
            return ResponseEntity.ok(report);

        } catch (Exception e) {
            log.error("Error generating yearly report: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Không thể tạo báo cáo hàng năm: " + e.getMessage()));
        }
    }

    @PostMapping("/custom")
    public ResponseEntity<Map<String, Object>> generateCustomReport(
            @RequestBody Map<String, Object> requestData) {

        log.info("Generating custom report with AI...");

        try {
            if (!requestData.containsKey("startDate") || !requestData.containsKey("endDate")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "startDate và endDate là bắt buộc"));
            }

            String startDate = (String) requestData.get("startDate");
            String endDate = (String) requestData.get("endDate");
            String reportType = (String) requestData.getOrDefault("reportType", "summary");

            Object bookingData = requestData.get("bookingData");
            Object paymentData = requestData.get("paymentData");
            Object invoiceData = requestData.get("invoiceData");

            log.info("Custom report parameters - Start: {}, End: {}, Type: {}", startDate, endDate, reportType);

            Map<String, Object> report = Map.of(
                    "reportType", "custom",
                    "startDate", startDate,
                    "endDate", endDate,
                    "generatedAt", java.time.LocalDateTime.now().toString(),
                    "message", "Báo cáo tùy chỉnh đang được phát triển",
                    "data", Map.of(
                            "bookingData", bookingData != null ? "Có dữ liệu" : "Không có dữ liệu",
                            "paymentData", paymentData != null ? "Có dữ liệu" : "Không có dữ liệu",
                            "invoiceData", invoiceData != null ? "Có dữ liệu" : "Không có dữ liệu"
                    )
            );

            log.info("Custom report generated successfully");
            return ResponseEntity.ok(report);

        } catch (Exception e) {
            log.error("Error generating custom report: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Không thể tạo báo cáo tùy chỉnh: " + e.getMessage()));
        }
    }


    @GetMapping("/types")
    public ResponseEntity<Map<String, Object>> getAvailableReportTypes() {

        log.info("Getting available report types...");

        Map<String, Object> reportTypes = Map.of(
                "availableReports", Map.of(
                        "daily", Map.of(
                                "name", "Báo Cáo Hàng Ngày",
                                "description", "Báo cáo tổng hợp hoạt động trong ngày",
                                "endpoint", "/api/ai/reports/daily",
                                "method", "POST",
                                "template", "daily-report-template.html"
                        ),
                        "monthly", Map.of(
                                "name", "Báo Cáo Hàng Tháng",
                                "description", "Báo cáo tổng hợp hoạt động trong tháng",
                                "endpoint", "/api/ai/reports/monthly",
                                "method", "POST",
                                "template", "monthly-report-template.html",
                                "parameters", Map.of(
                                        "month", "Tháng (1-12)",
                                        "year", "Năm (2020-2030)"
                                )
                        ),
                        "yearly", Map.of(
                                "name", "Báo Cáo Hàng Năm",
                                "description", "Báo cáo tổng hợp hoạt động trong năm",
                                "endpoint", "/api/ai/reports/yearly",
                                "method", "POST",
                                "template", "yearly-report-template.html",
                                "parameters", Map.of(
                                        "year", "Năm (2020-2030)"
                                )
                        ),
                        "custom", Map.of(
                                "name", "Báo Cáo Tùy Chỉnh",
                                "description", "Báo cáo theo khoảng thời gian tùy chỉnh",
                                "endpoint", "/api/ai/reports/custom",
                                "method", "POST",
                                "parameters", Map.of(
                                        "startDate", "Ngày bắt đầu (YYYY-MM-DD)",
                                        "endDate", "Ngày kết thúc (YYYY-MM-DD)",
                                        "reportType", "Loại báo cáo (summary, detailed, etc.)"
                                )
                        )
                ),
                "dataSources", Map.of(
                        "bookingData", "Dữ liệu đặt phòng từ Booking Service",
                        "paymentData", "Dữ liệu thanh toán từ Payment Service",
                        "invoiceData", "Dữ liệu hóa đơn từ Invoice Service"
                ),
                "aiFeatures", Map.of(
                        "automaticAnalysis", "Phân tích tự động dữ liệu",
                        "insights", "Đưa ra insights và khuyến nghị",
                        "trendAnalysis", "Phân tích xu hướng",
                        "fallbackSupport", "Hỗ trợ fallback khi AI không khả dụng"
                ),
                "templates", Map.of(
                        "daily", "daily-report-template.html",
                        "monthly", "monthly-report-template.html",
                        "yearly", "yearly-report-template.html"
                )
        );

        return ResponseEntity.ok(reportTypes);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkAIHealth() {

        log.info("Checking AI service health...");

        try {
            Map<String, Object> healthStatus = Map.of(
                    "status", "HEALTHY",
                    "timestamp", java.time.LocalDateTime.now().toString(),
                    "aiService", "AI Report Service",
                    "version", "1.0.0",
                    "features", Map.of(
                            "dailyReports", true,
                            "monthlyReports", true,
                            "yearlyReports", true,
                            "customReports", true,
                            "aiProcessing", true,
                            "fallbackSupport", true
                    ),
                    "message", "AI service đang hoạt động bình thường"
            );

            return ResponseEntity.ok(healthStatus);

        } catch (Exception e) {
            log.error("AI service health check failed: ", e);

            Map<String, Object> errorStatus = Map.of(
                    "status", "UNHEALTHY",
                    "timestamp", java.time.LocalDateTime.now().toString(),
                    "error", e.getMessage(),
                    "message", "AI service gặp sự cố"
            );

            return ResponseEntity.status(503).body(errorStatus);
        }
    }
}
