package com.poly.notification.management.service;

import com.poly.notification.management.repository.feign.AIClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendReportByAIService {

    private final AIClient aiClient;
    private final EmailService emailService;

    public Boolean sendReportDailyByAi(String userEmail) {
        log.info("Bắt đầu gửi báo cáo hàng ngày cho email: {}", userEmail);
        try {
            ResponseEntity<Map<String, Object>> response = aiClient.generateDailyReport();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> reportData = response.getBody();
                String subject = "Báo cáo AI hàng ngày - Nikka Hotel";
                emailService.sendHtmlEmail(userEmail, subject, "daily-report-template.html", reportData);
                log.info("Báo cáo hàng ngày đã được gửi thành công.");
                return true;
            }
            log.warn("Không thể lấy báo cáo hàng ngày từ AI client. Status: {}", response.getStatusCode());
            return false;
        } catch (Exception e) {
            log.error("Lỗi khi gửi báo cáo hàng ngày: {}", e.getMessage(), e);
            return false;
        }
    }

    public Boolean sendReportMonthlyByAi(String userEmail, int month, int year) {
        log.info("Bắt đầu gửi báo cáo hàng tháng ({}/{}) cho email: {}", month, year, userEmail);
        try {
            ResponseEntity<Map<String, Object>> response = aiClient.generateMonthlyReport(month, year);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> reportData = response.getBody();
                String subject = "Báo cáo AI hàng tháng - Nikka Hotel";
                emailService.sendHtmlEmail(userEmail, subject, "monthly-report-template.html", reportData);
                log.info("Báo cáo hàng tháng đã được gửi thành công.");
                return true;
            }
            log.warn("Không thể lấy báo cáo hàng tháng từ AI client. Status: {}", response.getStatusCode());
            return false;
        } catch (Exception e) {
            log.error("Lỗi khi gửi báo cáo hàng tháng: {}", e.getMessage(), e);
            return false;
        }
    }

    public Boolean sendReportYearlyByAi(String userEmail, int year) {
        log.info("Bắt đầu gửi báo cáo hàng năm ({}) cho email: {}", year, userEmail);
        try {
            ResponseEntity<Map<String, Object>> response = aiClient.generateYearlyReport(year);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> reportData = response.getBody();
                String subject = "Báo cáo AI hàng năm - Nikka Hotel";
                emailService.sendHtmlEmail(userEmail, subject, "yearly-report-template.html", reportData);
                log.info("Báo cáo hàng năm đã được gửi thành công.");
                return true;
            }
            log.warn("Không thể lấy báo cáo hàng năm từ AI client. Status: {}", response.getStatusCode());
            return false;
        } catch (Exception e) {
            log.error("Lỗi khi gửi báo cáo hàng năm: {}", e.getMessage(), e);
            return false;
        }
    }
}