package com.poly.promotion.application.dto.monitoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for aggregated performance metrics sent to the monitoring service.
 * Contains summarized performance data for multiple method executions.
 *
 * @author System
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceMetrics {

    /**
     * Unique identifier for this metrics entry.
     */
    private String id;

    /**
     * Timestamp when these metrics were collected.
     */
    private LocalDateTime collectionTimestamp;

    /**
     * Service name (for microservices identification).
     */
    private String serviceName;

    /**
     * Environment (dev, test, prod).
     */
    private String environment;

    /**
     * Time period covered by these metrics (e.g., "1 hour", "1 day").
     */
    private String timePeriod;

    /**
     * Total number of method executions in the time period.
     */
    private Long totalExecutions;

    /**
     * Total execution time in milliseconds for all methods.
     */
    private Long totalExecutionTimeMs;

    /**
     * Average execution time in milliseconds across all methods.
     */
    private Double averageExecutionTimeMs;

    /**
     * Minimum execution time in milliseconds.
     */
    private Long minExecutionTimeMs;

    /**
     * Maximum execution time in milliseconds.
     */
    private Long maxExecutionTimeMs;

    /**
     * Number of slow method executions (>1 second).
     */
    private Long slowMethodCount;

    /**
     * Number of very slow method executions (>5 seconds).
     */
    private Long verySlowMethodCount;

    /**
     * Number of failed method executions.
     */
    private Long failedExecutionCount;

    /**
     * Method-specific performance breakdown.
     */
    private Map<String, MethodPerformanceSummary> methodBreakdown;

    /**
     * Performance thresholds used for categorization.
     */
    private PerformanceThresholds thresholds;

    /**
     * Additional metadata for the performance metrics.
     */
    private Map<String, Object> metadata;

    /**
     * Inner class for method-specific performance summary.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MethodPerformanceSummary {
        private String methodName;
        private String className;
        private Long executionCount;
        private Double averageExecutionTimeMs;
        private Long minExecutionTimeMs;
        private Long maxExecutionTimeMs;
        private Long slowExecutionCount;
        private Long failedExecutionCount;
    }

    /**
     * Inner class for performance thresholds.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformanceThresholds {
        private Long slowMethodThresholdMs;
        private Long verySlowMethodThresholdMs;
        private Long criticalMethodThresholdMs;
    }
}

