package com.poly.promotion.application.client;

import com.poly.promotion.application.dto.monitoring.MethodExecutionMetrics;
import com.poly.promotion.application.dto.monitoring.PerformanceMetrics;
import com.poly.promotion.application.dto.monitoring.SystemHealthMetrics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Feign client interface for communicating with the external monitoring service.
 * This client will be used to send monitoring data to the centralized monitoring system.
 *
 * @author System
 * @since 1.0.0
 */
@FeignClient(
        name = "monitoring-service",
        url = "${monitoring.service.url:http://localhost:8081}",
        fallback = MonitoringServiceClientFallback.class
)
public interface MonitoringServiceClient {

    /**
     * Sends method execution metrics to the monitoring service.
     *
     * @param metrics the method execution metrics to send
     * @return response indicating success or failure
     */
    @PostMapping("/api/v1/monitoring/metrics/method-execution")
    ResponseEntity<String> sendMethodExecutionMetrics(@RequestBody MethodExecutionMetrics metrics);

    /**
     * Sends aggregated performance metrics to the monitoring service.
     *
     * @param metrics the aggregated performance metrics to send
     * @return response indicating success or failure
     */
    @PostMapping("/api/v1/monitoring/metrics/performance")
    ResponseEntity<String> sendPerformanceMetrics(@RequestBody PerformanceMetrics metrics);

    /**
     * Sends system health metrics to the monitoring service.
     *
     * @param metrics the system health metrics to send
     * @return response indicating success or failure
     */
    @PostMapping("/api/v1/monitoring/metrics/health")
    ResponseEntity<String> sendSystemHealthMetrics(@RequestBody SystemHealthMetrics metrics);

    /**
     * Sends audit log entry to the monitoring service.
     *
     * @param auditEntry the audit log entry to send
     * @return response indicating success or failure
     */
    @PostMapping("/api/v1/monitoring/audit")
    ResponseEntity<String> sendAuditLogEntry(@RequestBody String auditEntry);

    /**
     * Sends error/exception information to the monitoring service.
     *
     * @param errorInfo the error information to send
     * @return response indicating success or failure
     */
    @PostMapping("/api/v1/monitoring/errors")
    ResponseEntity<String> sendErrorInfo(@RequestBody String errorInfo);

    /**
     * Health check endpoint for the monitoring service.
     *
     * @return response indicating if the monitoring service is available
     */
    @GetMapping("/api/v1/monitoring/health")
    ResponseEntity<String> healthCheck();

    /**
     * Gets the status of the monitoring service.
     *
     * @return response with service status information
     */
    @GetMapping("/api/v1/monitoring/status")
    ResponseEntity<String> getServiceStatus();
}

