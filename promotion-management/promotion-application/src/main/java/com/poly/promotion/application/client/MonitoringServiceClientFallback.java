package com.poly.promotion.application.client;

import com.poly.promotion.application.dto.monitoring.MethodExecutionMetrics;
import com.poly.promotion.application.dto.monitoring.PerformanceMetrics;
import com.poly.promotion.application.dto.monitoring.SystemHealthMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for MonitoringServiceClient when the external monitoring service is unavailable.
 * Provides graceful degradation by logging locally instead of sending to external service.
 *
 * @author System
 * @since 1.0.0
 */
@Component
@Slf4j
public class MonitoringServiceClientFallback implements MonitoringServiceClient {

    @Override
    public ResponseEntity<String> sendMethodExecutionMetrics(MethodExecutionMetrics metrics) {
        log.warn("Monitoring service unavailable - logging method execution metrics locally: {}", metrics);
        return ResponseEntity.ok("Fallback: Metrics logged locally");
    }

    @Override
    public ResponseEntity<String> sendPerformanceMetrics(PerformanceMetrics metrics) {
        log.warn("Monitoring service unavailable - logging performance metrics locally: {}", metrics);
        return ResponseEntity.ok("Fallback: Performance metrics logged locally");
    }

    @Override
    public ResponseEntity<String> sendSystemHealthMetrics(SystemHealthMetrics metrics) {
        log.warn("Monitoring service unavailable - logging system health metrics locally: {}", metrics);
        return ResponseEntity.ok("Fallback: Health metrics logged locally");
    }

    @Override
    public ResponseEntity<String> sendAuditLogEntry(String auditEntry) {
        log.warn("Monitoring service unavailable - logging audit entry locally: {}", auditEntry);
        return ResponseEntity.ok("Fallback: Audit entry logged locally");
    }

    @Override
    public ResponseEntity<String> sendErrorInfo(String errorInfo) {
        log.warn("Monitoring service unavailable - logging error info locally: {}", errorInfo);
        return ResponseEntity.ok("Fallback: Error info logged locally");
    }

    @Override
    public ResponseEntity<String> healthCheck() {
        log.warn("Monitoring service unavailable - health check failed");
        return ResponseEntity.status(503).body("Monitoring service unavailable");
    }

    @Override
    public ResponseEntity<String> getServiceStatus() {
        log.warn("Monitoring service unavailable - cannot get service status");
        return ResponseEntity.status(503).body("Monitoring service unavailable");
    }
}

