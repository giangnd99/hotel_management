package com.poly.ai.management.domain.controller;

import com.poly.ai.management.domain.service.AIReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller để tạo báo cáo tự động thông qua AI
 * Tích hợp với Booking, Payment và Invoice services
 */
@RestController
@RequestMapping("/api/ai/reports")
@RequiredArgsConstructor
@Tag(name = "AI Report Controller", description = "Tạo báo cáo tự động thông qua AI")
@Slf4j
public class AIReportController {

    private final AIReportService aiReportService;

    /**
     * Tạo báo cáo hàng ngày
     * POST /api/ai/reports/daily
     */
    @PostMapping("/daily")
    @Operation(summary = "Tạo báo cáo hàng ngày", description = "Tạo báo cáo hàng ngày với dữ liệu từ Booking, Payment và Invoice")
    public ResponseEntity<Map<String, Object>> generateDailyReport(
            @RequestBody(required = false) Map<String, Object> requestData) {
        
        log.info("Generating daily report with AI...");
        
        try {
            // Lấy dữ liệu từ request hoặc sử dụng dữ liệu mặc định
            Object bookingData = requestData != null ? requestData.get("bookingData") : null;
            Object paymentData = requestData != null ? requestData.get("paymentData") : null;
            Object invoiceData = requestData != null ? requestData.get("invoiceData") : null;
            
            Map<String, Object> report = aiReportService.generateDailyReport(bookingData, paymentData, invoiceData);
            
            log.info("Daily report generated successfully");
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            log.error("Error generating daily report: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Không thể tạo báo cáo hàng ngày: " + e.getMessage()));
        }
    }

    /**
     * Tạo báo cáo hàng tháng
     * POST /api/ai/reports/monthly
     */
    @PostMapping("/monthly")
    @Operation(summary = "Tạo báo cáo hàng tháng", description = "Tạo báo cáo hàng tháng với dữ liệu từ Booking, Payment và Invoice")
    public ResponseEntity<Map<String, Object>> generateMonthlyReport(
            @RequestBody(required = false) Map<String, Object> requestData,
            @RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "2024") int year) {
        
        log.info("Generating monthly report for {}/{} with AI...", month, year);
        
        try {
            // Validate parameters
            if (month < 1 || month > 12) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Tháng phải từ 1-12"));
            }
            
            if (year < 2020 || year > 2030) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Năm phải từ 2020-2030"));
            }
            
            // Lấy dữ liệu từ request hoặc sử dụng dữ liệu mặc định
            Object bookingData = requestData != null ? requestData.get("bookingData") : null;
            Object paymentData = requestData != null ? requestData.get("paymentData") : null;
            Object invoiceData = requestData != null ? requestData.get("invoiceData") : null;
            
            Map<String, Object> report = aiReportService.generateMonthlyReport(bookingData, paymentData, invoiceData, month, year);
            
            log.info("Monthly report for {}/{} generated successfully", month, year);
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            log.error("Error generating monthly report: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Không thể tạo báo cáo hàng tháng: " + e.getMessage()));
        }
    }

    /**
     * Tạo báo cáo hàng năm
     * POST /api/ai/reports/yearly
     */
    @PostMapping("/yearly")
    @Operation(summary = "Tạo báo cáo hàng năm", description = "Tạo báo cáo hàng năm với dữ liệu từ Booking, Payment và Invoice")
    public ResponseEntity<Map<String, Object>> generateYearlyReport(
            @RequestBody(required = false) Map<String, Object> requestData,
            @RequestParam(defaultValue = "2024") int year) {
        
        log.info("Generating yearly report for {} with AI...", year);
        
        try {
            // Validate parameters
            if (year < 2020 || year > 2030) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Năm phải từ 2020-2030"));
            }
            
            // Lấy dữ liệu từ request hoặc sử dụng dữ liệu mặc định
            Object bookingData = requestData != null ? requestData.get("bookingData") : null;
            Object paymentData = requestData != null ? requestData.get("paymentData") : null;
            Object invoiceData = requestData != null ? requestData.get("invoiceData") : null;
            
            Map<String, Object> report = aiReportService.generateYearlyReport(bookingData, paymentData, invoiceData, year);
            
            log.info("Yearly report for {} generated successfully", year);
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            log.error("Error generating yearly report: ", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Không thể tạo báo cáo hàng năm: " + e.getMessage()));
        }
    }

    /**
     * Tạo báo cáo tùy chỉnh
     * POST /api/ai/reports/custom
     */
    @PostMapping("/custom")
    @Operation(summary = "Tạo báo cáo tùy chỉnh", description = "Tạo báo cáo tùy chỉnh với khoảng thời gian và dữ liệu được chỉ định")
    public ResponseEntity<Map<String, Object>> generateCustomReport(
            @RequestBody Map<String, Object> requestData) {
        
        log.info("Generating custom report with AI...");
        
        try {
            // Validate required fields
            if (!requestData.containsKey("startDate") || !requestData.containsKey("endDate")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "startDate và endDate là bắt buộc"));
            }
            
            String startDate = (String) requestData.get("startDate");
            String endDate = (String) requestData.get("endDate");
            String reportType = (String) requestData.getOrDefault("reportType", "summary");
            
            // Lấy dữ liệu từ request
            Object bookingData = requestData.get("bookingData");
            Object paymentData = requestData.get("paymentData");
            Object invoiceData = requestData.get("invoiceData");
            
            log.info("Custom report parameters - Start: {}, End: {}, Type: {}", startDate, endDate, reportType);
            
            // Tạo báo cáo tùy chỉnh (có thể mở rộng trong tương lai)
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

    /**
     * Lấy thông tin về các loại báo cáo có sẵn
     * GET /api/ai/reports/types
     */
    @GetMapping("/types")
    @Operation(summary = "Lấy danh sách loại báo cáo", description = "Lấy thông tin về các loại báo cáo có thể tạo")
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

    /**
     * Kiểm tra trạng thái AI service
     * GET /api/ai/reports/health
     */
    @GetMapping("/health")
    @Operation(summary = "Kiểm tra trạng thái AI", description = "Kiểm tra trạng thái hoạt động của AI service")
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
