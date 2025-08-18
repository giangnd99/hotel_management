package com.poly.promotion.application.service;

import com.poly.promotion.application.dto.monitoring.MethodExecutionMetrics;
import com.poly.promotion.application.dto.monitoring.PerformanceMetrics;
import com.poly.promotion.application.dto.monitoring.SystemHealthMetrics;
import com.poly.promotion.data.access.jpaentity.MonitoringDataEntity;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing monitoring data persistence and retrieval.
 * Provides local storage for monitoring data to prevent data loss.
 *
 * @author System
 * @since 1.0.0
 */
public interface MonitoringDataService {

    /**
     * Saves method execution metrics locally.
     *
     * @param metrics the method execution metrics to save
     * @return the saved monitoring data entity
     */
    MonitoringDataEntity saveMethodExecutionMetrics(MethodExecutionMetrics metrics);

    /**
     * Saves performance metrics locally.
     *
     * @param metrics the performance metrics to save
     * @return the saved monitoring data entity
     */
    MonitoringDataEntity savePerformanceMetrics(PerformanceMetrics metrics);

    /**
     * Saves system health metrics locally.
     *
     * @param metrics the system health metrics to save
     * @return the saved monitoring data entity
     */
    MonitoringDataEntity saveSystemHealthMetrics(SystemHealthMetrics metrics);

    /**
     * Saves audit log entry locally.
     *
     * @param auditEntry the audit log entry to save
     * @return the saved monitoring data entity
     */
    MonitoringDataEntity saveAuditLogEntry(String auditEntry);

    /**
     * Saves error information locally.
     *
     * @param errorInfo the error information to save
     * @return the saved monitoring data entity
     */
    MonitoringDataEntity saveErrorInfo(String errorInfo);

    /**
     * Retrieves all unsynced monitoring data.
     *
     * @return list of unsynced monitoring data
     */
    List<MonitoringDataEntity> getUnsyncedData();

    /**
     * Retrieves unsynced monitoring data by type.
     *
     * @param dataType the type of monitoring data
     * @return list of unsynced monitoring data of the specified type
     */
    List<MonitoringDataEntity> getUnsyncedDataByType(MonitoringDataEntity.MonitoringDataType dataType);

    /**
     * Marks monitoring data as synced.
     *
     * @param id the ID of the monitoring data entity
     */
    void markAsSynced(Long id);

    /**
     * Updates sync attempt information for failed syncs.
     *
     * @param id the ID of the monitoring data entity
     * @param errorMessage the error message from the failed sync
     */
    void updateSyncAttemptInfo(Long id, String errorMessage);

    /**
     * Retrieves monitoring data by ID.
     *
     * @param id the ID of the monitoring data entity
     * @return optional containing the monitoring data entity
     */
    Optional<MonitoringDataEntity> getById(Long id);

    /**
     * Retrieves monitoring data by external ID.
     *
     * @param externalId the external ID
     * @return optional containing the monitoring data entity
     */
    Optional<MonitoringDataEntity> getByExternalId(String externalId);

    /**
     * Counts unsynced monitoring data.
     *
     * @return count of unsynced data
     */
    long countUnsyncedData();

    /**
     * Counts monitoring data by type.
     *
     * @param dataType the type of monitoring data
     * @return count of data of the specified type
     */
    long countByDataType(MonitoringDataEntity.MonitoringDataType dataType);

    /**
     * Cleans up old monitoring data.
     *
     * @param retentionDays the number of days to retain data
     * @return number of deleted records
     */
    int cleanupOldData(int retentionDays);

    /**
     * Cleans up old synced monitoring data.
     *
     * @param retentionDays the number of days to retain synced data
     * @return number of deleted records
     */
    int cleanupOldSyncedData(int retentionDays);
}
