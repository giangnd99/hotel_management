package com.poly.notification.management.repository.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "ai-service", url = "http://localhost:8088/api/ai/reports")
public interface AIClient {

    @PostMapping("/daily")
    ResponseEntity<Map<String, Object>> generateDailyReport();

    @PostMapping("/monthly")
    ResponseEntity<Map<String, Object>> generateMonthlyReport(
            @RequestParam(defaultValue = "1") int month,
            @RequestParam(defaultValue = "2024") int year);

    @PostMapping("/yearly")
    ResponseEntity<Map<String, Object>> generateYearlyReport(
            @RequestParam(defaultValue = "2024") int year);
}
