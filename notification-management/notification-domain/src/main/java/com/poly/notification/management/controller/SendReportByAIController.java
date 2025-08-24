package com.poly.notification.management.controller;

import com.poly.notification.management.service.SendReportByAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/email/report")
@RequiredArgsConstructor
@Slf4j
public class SendReportByAIController {

    private final SendReportByAIService sendReportByAIService;

    @PostMapping("/daily")
    public ResponseEntity<Map<String, String>> sendDailyReportByAi(@RequestParam String email) {
        log.info("Yêu cầu gửi báo cáo hàng ngày cho email: {}", email);
        Map<String, String> response = new HashMap<>();
        try {
            boolean success = sendReportByAIService.sendReportDailyByAi(email);
            if (success) {
                response.put("message", "Báo cáo hàng ngày đã được gửi thành công.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Gửi báo cáo hàng ngày thất bại. Vui lòng thử lại sau.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("Lỗi trong quá trình gửi báo cáo hàng ngày: {}", e.getMessage(), e);
            response.put("message", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/monthly")
    public ResponseEntity<Map<String, String>> sendMonthlyReportByAi(
            @RequestParam String email,
            @RequestParam int month,
            @RequestParam int year) {
        log.info("Yêu cầu gửi báo cáo hàng tháng ({}/{}) cho email: {}", month, year, email);
        Map<String, String> response = new HashMap<>();
        try {
            boolean success = sendReportByAIService.sendReportMonthlyByAi(email, month, year);
            if (success) {
                response.put("message", "Báo cáo hàng tháng đã được gửi thành công.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Gửi báo cáo hàng tháng thất bại. Vui lòng thử lại sau.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("Lỗi trong quá trình gửi báo cáo hàng tháng: {}", e.getMessage(), e);
            response.put("message", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/yearly")
    public ResponseEntity<Map<String, String>> sendYearlyReportByAi(
            @RequestParam String email,
            @RequestParam int year) {
        log.info("Yêu cầu gửi báo cáo hàng năm ({}) cho email: {}", year, email);
        Map<String, String> response = new HashMap<>();
        try {
            boolean success = sendReportByAIService.sendReportYearlyByAi(email, year);
            if (success) {
                response.put("message", "Báo cáo hàng năm đã được gửi thành công.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Gửi báo cáo hàng năm thất bại. Vui lòng thử lại sau.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("Lỗi trong quá trình gửi báo cáo hàng năm: {}", e.getMessage(), e);
            response.put("message", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}