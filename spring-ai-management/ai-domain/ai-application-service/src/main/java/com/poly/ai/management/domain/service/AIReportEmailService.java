package com.poly.ai.management.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service để tích hợp với Notification Management
 * Gửi email báo cáo tự động thông qua AI
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AIReportEmailService {

    private final RestTemplate restTemplate;
    
    @Value("${notification.service.url:http://localhost:8080}")
    private String notificationServiceUrl;
    
    @Value("${notification.service.endpoint:/api/notifications/send-email}")
    private String notificationEndpoint;
    
    @Value("${ai.report.email.recipients:admin@luxuryhotel.com,manager@luxuryhotel.com}")
    private String defaultRecipients;
    
    @Value("${ai.report.email.subject.prefix:[AI Report]}")
    private String emailSubjectPrefix;

    /**
     * Gửi báo cáo hàng ngày qua email
     */
    public boolean sendDailyReportEmail(Map<String, Object> reportData, List<String> recipients) {
        log.info("Sending daily report email...");
        
        try {
            String subject = buildDailyReportSubject();
            String templateName = "daily-report-template";
            
            Map<String, Object> emailData = buildEmailData(reportData, recipients, subject, templateName);
            
            boolean sent = sendEmailNotification(emailData);
            
            if (sent) {
                log.info("Daily report email sent successfully to {} recipients", recipients.size());
            } else {
                log.error("Failed to send daily report email");
            }
            
            return sent;
            
        } catch (Exception e) {
            log.error("Error sending daily report email: ", e);
            return false;
        }
    }

    /**
     * Gửi báo cáo hàng tháng qua email
     */
    public boolean sendMonthlyReportEmail(Map<String, Object> reportData, List<String> recipients, int month, int year) {
        log.info("Sending monthly report email for {}/{}...", month, year);
        
        try {
            String subject = buildMonthlyReportSubject(month, year);
            String templateName = "monthly-report-template";
            
            Map<String, Object> emailData = buildEmailData(reportData, recipients, subject, templateName);
            
            boolean sent = sendEmailNotification(emailData);
            
            if (sent) {
                log.info("Monthly report email for {}/{} sent successfully to {} recipients", month, year, recipients.size());
            } else {
                log.error("Failed to send monthly report email for {}/{}", month, year);
            }
            
            return sent;
            
        } catch (Exception e) {
            log.error("Error sending monthly report email: ", e);
            return false;
        }
    }

    /**
     * Gửi báo cáo hàng năm qua email
     */
    public boolean sendYearlyReportEmail(Map<String, Object> reportData, List<String> recipients, int year) {
        log.info("Sending yearly report email for {}...", year);
        
        try {
            String subject = buildYearlyReportSubject(year);
            String templateName = "yearly-report-template";
            
            Map<String, Object> emailData = buildEmailData(reportData, recipients, subject, templateName);
            
            boolean sent = sendEmailNotification(emailData);
            
            if (sent) {
                log.info("Yearly report email for {} sent successfully to {} recipients", year, recipients.size());
            } else {
                log.error("Failed to send yearly report email for {}", year);
            }
            
            return sent;
            
        } catch (Exception e) {
            log.error("Error sending yearly report email: ", e);
            return false;
        }
    }

    /**
     * Gửi báo cáo tùy chỉnh qua email
     */
    public boolean sendCustomReportEmail(Map<String, Object> reportData, List<String> recipients, 
                                       String startDate, String endDate, String reportType) {
        log.info("Sending custom report email from {} to {}...", startDate, endDate);
        
        try {
            String subject = buildCustomReportSubject(startDate, endDate, reportType);
            String templateName = "daily-report-template"; // Sử dụng template mặc định
            
            Map<String, Object> emailData = buildEmailData(reportData, recipients, subject, templateName);
            
            boolean sent = sendEmailNotification(emailData);
            
            if (sent) {
                log.info("Custom report email sent successfully to {} recipients", recipients.size());
            } else {
                log.error("Failed to send custom report email");
            }
            
            return sent;
            
        } catch (Exception e) {
            log.error("Error sending custom report email: ", e);
            return false;
        }
    }

    /**
     * Gửi báo cáo hàng ngày tự động (scheduled)
     */
    public boolean sendScheduledDailyReport() {
        log.info("Sending scheduled daily report...");
        
        try {
            // Sử dụng recipients mặc định
            List<String> recipients = parseDefaultRecipients();
            
            // Tạo dữ liệu báo cáo mặc định
            Map<String, Object> defaultReportData = createDefaultDailyReportData();
            
            return sendDailyReportEmail(defaultReportData, recipients);
            
        } catch (Exception e) {
            log.error("Error sending scheduled daily report: ", e);
            return false;
        }
    }

    /**
     * Gửi báo cáo hàng tháng tự động (scheduled)
     */
    public boolean sendScheduledMonthlyReport() {
        log.info("Sending scheduled monthly report...");
        
        try {
            LocalDate now = LocalDate.now();
            int month = now.getMonthValue();
            int year = now.getYear();
            
            // Sử dụng recipients mặc định
            List<String> recipients = parseDefaultRecipients();
            
            // Tạo dữ liệu báo cáo mặc định
            Map<String, Object> defaultReportData = createDefaultMonthlyReportData(month, year);
            
            return sendMonthlyReportEmail(defaultReportData, recipients, month, year);
            
        } catch (Exception e) {
            log.error("Error sending scheduled monthly report: ", e);
            return false;
        }
    }

    /**
     * Gửi báo cáo hàng năm tự động (scheduled)
     */
    public boolean sendScheduledYearlyReport() {
        log.info("Sending scheduled yearly report...");
        
        try {
            int year = LocalDate.now().getYear();
            
            // Sử dụng recipients mặc định
            List<String> recipients = parseDefaultRecipients();
            
            // Tạo dữ liệu báo cáo mặc định
            Map<String, Object> defaultReportData = createDefaultYearlyReportData(year);
            
            return sendYearlyReportEmail(defaultReportData, recipients, year);
            
        } catch (Exception e) {
            log.error("Error sending scheduled yearly report: ", e);
            return false;
        }
    }

    /**
     * Xây dựng dữ liệu email
     */
    private Map<String, Object> buildEmailData(Map<String, Object> reportData, List<String> recipients, 
                                             String subject, String templateName) {
        Map<String, Object> emailData = new HashMap<>();
        
        emailData.put("to", recipients);
        emailData.put("subject", subject);
        emailData.put("templateName", templateName);
        emailData.put("templateData", reportData);
        emailData.put("priority", "normal");
        emailData.put("scheduledAt", LocalDateTime.now().toString());
        
        return emailData;
    }

    /**
     * Gửi thông báo email thông qua Notification Service
     */
    private boolean sendEmailNotification(Map<String, Object> emailData) {
        try {
            String url = notificationServiceUrl + notificationEndpoint;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailData, headers);
            
            log.info("Sending email notification to: {}", url);
            
            // Gọi Notification Service
            var response = restTemplate.postForEntity(url, request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Email notification sent successfully");
                return true;
            } else {
                log.error("Failed to send email notification. Status: {}", response.getStatusCode());
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error calling notification service: ", e);
            return false;
        }
    }

    /**
     * Xây dựng subject cho báo cáo hàng ngày
     */
    private String buildDailyReportSubject() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return String.format("%s Báo Cáo Hàng Ngày - %s", emailSubjectPrefix, date);
    }

    /**
     * Xây dựng subject cho báo cáo hàng tháng
     */
    private String buildMonthlyReportSubject(int month, int year) {
        return String.format("%s Báo Cáo Hàng Tháng - Tháng %d/%d", emailSubjectPrefix, month, year);
    }

    /**
     * Xây dựng subject cho báo cáo hàng năm
     */
    private String buildYearlyReportSubject(int year) {
        return String.format("%s Báo Cáo Hàng Năm - %d", emailSubjectPrefix, year);
    }

    /**
     * Xây dựng subject cho báo cáo tùy chỉnh
     */
    private String buildCustomReportSubject(String startDate, String endDate, String reportType) {
        return String.format("%s Báo Cáo Tùy Chỉnh - %s đến %s (%s)", 
            emailSubjectPrefix, startDate, endDate, reportType);
    }

    /**
     * Parse recipients mặc định từ configuration
     */
    private List<String> parseDefaultRecipients() {
        return List.of(defaultRecipients.split(","));
    }

    /**
     * Tạo dữ liệu báo cáo hàng ngày mặc định
     */
    private Map<String, Object> createDefaultDailyReportData() {
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("reportDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        reportData.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        reportData.put("message", "Báo cáo hàng ngày được tạo tự động bởi AI");
        return reportData;
    }

    /**
     * Tạo dữ liệu báo cáo hàng tháng mặc định
     */
    private Map<String, Object> createDefaultMonthlyReportData(int month, int year) {
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("reportMonth", String.format("Tháng %d/%d", month, year));
        reportData.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        reportData.put("message", "Báo cáo hàng tháng được tạo tự động bởi AI");
        return reportData;
    }

    /**
     * Tạo dữ liệu báo cáo hàng năm mặc định
     */
    private Map<String, Object> createDefaultYearlyReportData(int year) {
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("reportYear", String.valueOf(year));
        reportData.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        reportData.put("message", "Báo cáo hàng năm được tạo tự động bởi AI");
        return reportData;
    }

    /**
     * Kiểm tra kết nối với Notification Service
     */
    public boolean checkNotificationServiceHealth() {
        try {
            String healthUrl = notificationServiceUrl + "/actuator/health";
            
            var response = restTemplate.getForEntity(healthUrl, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Notification service is healthy");
                return true;
            } else {
                log.warn("Notification service health check failed. Status: {}", response.getStatusCode());
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error checking notification service health: ", e);
            return false;
        }
    }

    /**
     * Lấy thông tin cấu hình email
     */
    public Map<String, Object> getEmailConfiguration() {
        Map<String, Object> config = new HashMap<>();
        
        config.put("notificationServiceUrl", notificationServiceUrl);
        config.put("notificationEndpoint", notificationEndpoint);
        config.put("defaultRecipients", defaultRecipients);
        config.put("emailSubjectPrefix", emailSubjectPrefix);
        config.put("serviceHealth", checkNotificationServiceHealth());
        
        return config;
    }
}
