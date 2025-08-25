package com.poly.promotion.data.access.jpaentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA entity for storing monitoring data locally.
 * Prevents data loss when external monitoring service is unavailable.
 *
 * @author System
 * @since 1.0.0
 */
@Entity
@Table(name = "monitoring_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false)
    private MonitoringDataType dataType;

    @Column(name = "external_id")
    private String externalId;

    @Column(name = "data_content", columnDefinition = "TEXT", nullable = false)
    private String dataContent;

    @Column(name = "synced", nullable = false)
    private Boolean synced = false;

    @Column(name = "sync_attempts")
    private Integer syncAttempts = 0;

    @Column(name = "last_sync_attempt")
    private LocalDateTime lastSyncAttempt;

    @Column(name = "last_sync_error")
    private String lastSyncError;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    /**
     * Enum for different types of monitoring data.
     */
    public enum MonitoringDataType {
        METHOD_EXECUTION_METRICS,
        PERFORMANCE_METRICS,
        SYSTEM_HEALTH_METRICS,
        AUDIT_LOG_ENTRY,
        ERROR_INFO
    }
}
