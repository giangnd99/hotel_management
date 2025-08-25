package com.poly.promotion.application.dto.monitoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for system health metrics sent to the monitoring service.
 * Contains system health and resource utilization information.
 *
 * @author System
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemHealthMetrics {

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
     * Overall system health status.
     */
    private HealthStatus status;

    /**
     * JVM version information.
     */
    private String jvmVersion;

    /**
     * Available memory in bytes.
     */
    private Long availableMemoryBytes;

    /**
     * Total memory in bytes.
     */
    private Long totalMemoryBytes;

    /**
     * Maximum memory in bytes.
     */
    private Long maxMemoryBytes;

    /**
     * Memory usage percentage.
     */
    private Double memoryUsagePercentage;

    /**
     * Number of active threads.
     */
    private Integer activeThreadCount;

    /**
     * Peak thread count.
     */
    private Integer peakThreadCount;

    /**
     * Total started thread count.
     */
    private Long totalStartedThreadCount;

    /**
     * System uptime in milliseconds.
     */
    private Long uptimeMs;

    /**
     * CPU usage percentage (if available).
     */
    private Double cpuUsagePercentage;

    /**
     * Disk usage information.
     */
    private Map<String, DiskUsage> diskUsage;

    /**
     * Database connection pool status (if applicable).
     */
    private DatabaseConnectionStatus databaseStatus;

    /**
     * Additional health indicators.
     */
    private Map<String, Object> healthIndicators;

    /**
     * Additional metadata for the health metrics.
     */
    private Map<String, Object> metadata;

    /**
     * Enum for health status values.
     */
    public enum HealthStatus {
        HEALTHY, DEGRADED, UNHEALTHY, UNKNOWN
    }

    /**
     * Inner class for disk usage information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiskUsage {
        private String mountPoint;
        private Long totalSpaceBytes;
        private Long usableSpaceBytes;
        private Long freeSpaceBytes;
        private Double usagePercentage;
    }

    /**
     * Inner class for database connection status.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseConnectionStatus {
        private Boolean isConnected;
        private Integer activeConnections;
        private Integer maxConnections;
        private Integer idleConnections;
        private Long connectionWaitTimeMs;
        private String databaseType;
        private String databaseVersion;
    }
}

