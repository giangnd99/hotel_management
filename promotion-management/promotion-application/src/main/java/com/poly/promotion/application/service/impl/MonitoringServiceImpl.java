package com.poly.promotion.application.service.impl;

import com.poly.promotion.application.client.MonitoringServiceClient;
import com.poly.promotion.application.dto.monitoring.MethodExecutionMetrics;
import com.poly.promotion.application.dto.monitoring.PerformanceMetrics;
import com.poly.promotion.application.dto.monitoring.SystemHealthMetrics;
import com.poly.promotion.application.service.MonitoringDataService;
import com.poly.promotion.application.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of MonitoringService that uses Feign client to send monitoring data
 * to the external monitoring service.
 *
 * @author System
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringServiceImpl implements MonitoringService {

    private final MonitoringServiceClient monitoringServiceClient;
    private final MonitoringDataService monitoringDataService;

    @Value("${spring.application.name:promotion-management}")
    private String serviceName;

    @Value("${spring.profiles.active:dev}")
    private String environment;

    @Override
    @Async
    public void sendMethodExecutionMetrics(MethodExecutionMetrics metrics) {
        try {
            // Store locally first to prevent data loss
            monitoringDataService.saveMethodExecutionMetrics(metrics);
            
            // Enrich metrics with service information
            MethodExecutionMetrics enrichedMetrics = enrichMetrics(metrics);
            
            log.debug("Sending method execution metrics to monitoring service: {}", enrichedMetrics);
            monitoringServiceClient.sendMethodExecutionMetrics(enrichedMetrics);
            log.debug("Successfully sent method execution metrics to monitoring service");
        } catch (Exception e) {
            log.error("Failed to send method execution metrics to monitoring service", e);
            // Data is safe locally, will be retried by sync service
        }
    }

    @Override
    @Async
    public void sendPerformanceMetrics(PerformanceMetrics metrics) {
        try {
            // Store locally first to prevent data loss
            monitoringDataService.savePerformanceMetrics(metrics);
            
            // Enrich metrics with service information
            PerformanceMetrics enrichedMetrics = enrichMetrics(metrics);
            
            log.debug("Sending performance metrics to monitoring service: {}", enrichedMetrics);
            monitoringServiceClient.sendPerformanceMetrics(enrichedMetrics);
            log.debug("Successfully sent performance metrics to monitoring service");
        } catch (Exception e) {
            log.error("Failed to send performance metrics to monitoring service", e);
            // Data is safe locally, will be retried by sync service
        }
    }

    @Override
    @Async
    public void sendSystemHealthMetrics(SystemHealthMetrics metrics) {
        try {
            // Store locally first to prevent data loss
            monitoringDataService.saveSystemHealthMetrics(metrics);
            
            // Enrich metrics with service information
            SystemHealthMetrics enrichedMetrics = enrichMetrics(metrics);
            
            log.debug("Sending system health metrics to monitoring service: {}", enrichedMetrics);
            monitoringServiceClient.sendSystemHealthMetrics(enrichedMetrics);
            log.debug("Successfully sent system health metrics to monitoring service");
        } catch (Exception e) {
            log.error("Failed to send system health metrics to monitoring service", e);
            // Data is safe locally, will be retried by sync service
        }
    }

    @Override
    @Async
    public void sendAuditLogEntry(String auditEntry) {
        try {
            // Store locally first to prevent data loss
            monitoringDataService.saveAuditLogEntry(auditEntry);
            
            log.debug("Sending audit log entry to monitoring service: {}", auditEntry);
            monitoringServiceClient.sendAuditLogEntry(auditEntry);
            log.debug("Successfully sent audit log entry to monitoring service");
        } catch (Exception e) {
            log.error("Failed to send audit log entry to monitoring service", e);
            // Data is safe locally, will be retried by sync service
        }
    }

    @Override
    @Async
    public void sendErrorInfo(String errorInfo) {
        try {
            // Store locally first to prevent data loss
            monitoringDataService.saveErrorInfo(errorInfo);
            
            log.debug("Sending error info to monitoring service: {}", errorInfo);
            monitoringServiceClient.sendErrorInfo(errorInfo);
            log.debug("Successfully sent error info to monitoring service");
        } catch (Exception e) {
            log.error("Failed to send error info to monitoring service", e);
            // Data is safe locally, will be retried by sync service
        }
    }

    @Override
    public boolean isMonitoringServiceAvailable() {
        try {
            var response = monitoringServiceClient.healthCheck();
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.debug("Monitoring service health check failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Enriches method execution metrics with service information.
     *
     * @param metrics the original metrics
     * @return enriched metrics with service information
     */
    private MethodExecutionMetrics enrichMetrics(MethodExecutionMetrics metrics) {
        if (metrics.getId() == null) {
            metrics.setId(UUID.randomUUID().toString());
        }
        if (metrics.getServiceName() == null) {
            metrics.setServiceName(serviceName);
        }
        if (metrics.getEnvironment() == null) {
            metrics.setEnvironment(environment);
        }
        return metrics;
    }

    /**
     * Enriches performance metrics with service information.
     *
     * @param metrics the original metrics
     * @return enriched metrics with service information
     */
    private PerformanceMetrics enrichMetrics(PerformanceMetrics metrics) {
        if (metrics.getId() == null) {
            metrics.setId(UUID.randomUUID().toString());
        }
        if (metrics.getServiceName() == null) {
            metrics.setServiceName(serviceName);
        }
        if (metrics.getEnvironment() == null) {
            metrics.setEnvironment(environment);
        }
        return metrics;
    }

    /**
     * Enriches system health metrics with service information.
     *
     * @param metrics the original metrics
     * @return enriched metrics with service information
     */
    private SystemHealthMetrics enrichMetrics(SystemHealthMetrics metrics) {
        if (metrics.getId() == null) {
            metrics.setId(UUID.randomUUID().toString());
        }
        if (metrics.getServiceName() == null) {
            metrics.setServiceName(serviceName);
        }
        if (metrics.getEnvironment() == null) {
            metrics.setEnvironment(environment);
        }
        return metrics;
    }
}

