package com.poly.promotion.application.dto.monitoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for method execution metrics sent to the monitoring service.
 * Contains performance data for individual method executions.
 *
 * @author System
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodExecutionMetrics {

    /**
     * Unique identifier for this metrics entry.
     */
    private String id;

    /**
     * Name of the method being monitored.
     */
    private String methodName;

    /**
     * Class name containing the method.
     */
    private String className;

    /**
     * Full method signature.
     */
    private String methodSignature;

    /**
     * Execution time in milliseconds.
     */
    private Long executionTimeMs;

    /**
     * Timestamp when the method execution started.
     */
    private LocalDateTime startTime;

    /**
     * Timestamp when the method execution completed.
     */
    private LocalDateTime endTime;

    /**
     * Whether the method execution was successful.
     */
    private Boolean success;

    /**
     * Error message if the method execution failed.
     */
    private String errorMessage;

    /**
     * Exception class name if an error occurred.
     */
    private String exceptionClassName;

    /**
     * Service name (for microservices identification).
     */
    private String serviceName;

    /**
     * Environment (dev, test, prod).
     */
    private String environment;

    /**
     * Additional metadata for the method execution.
     */
    private String metadata;
}

