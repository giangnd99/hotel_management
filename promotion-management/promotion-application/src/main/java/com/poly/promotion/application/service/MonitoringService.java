package com.poly.promotion.application.service;

import com.poly.promotion.application.dto.monitoring.MethodExecutionMetrics;
import com.poly.promotion.application.dto.monitoring.PerformanceMetrics;
import com.poly.promotion.application.dto.monitoring.SystemHealthMetrics;

/**
 * Service interface for sending monitoring and performance data to external monitoring service.
 * This service will be implemented using Feign client to communicate with the monitoring service.
 *
 * @author System
 * @since 1.0.0
 */
public interface MonitoringService {

    /**
     * Sends method execution metrics to the monitoring service.
     * Called after each method execution to track performance.
     *
     * @param metrics the method execution metrics to send
     */
    void sendMethodExecutionMetrics(MethodExecutionMetrics metrics);

    /**
     * Sends aggregated performance metrics to the monitoring service.
     * Called periodically or when metrics are requested.
     *
     * @param metrics the aggregated performance metrics to send
     */
    void sendPerformanceMetrics(PerformanceMetrics metrics);

    /**
     * Sends system health metrics to the monitoring service.
     * Called periodically for health monitoring.
     *
     * @param metrics the system health metrics to send
     */
    void sendSystemHealthMetrics(SystemHealthMetrics metrics);

    /**
     * Sends audit log entry to the monitoring service.
     * Called for important business operations.
     *
     * @param auditEntry the audit log entry to send
     */
    void sendAuditLogEntry(String auditEntry);

    /**
     * Sends error/exception information to the monitoring service.
     * Called when exceptions occur for error tracking.
     *
     * @param errorInfo the error information to send
     */
    void sendErrorInfo(String errorInfo);

    /**
     * Checks if the monitoring service is available.
     * Used for health checks and fallback decisions.
     *
     * @return true if the monitoring service is available, false otherwise
     */
    boolean isMonitoringServiceAvailable();
}

