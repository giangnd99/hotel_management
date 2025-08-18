package com.poly.promotion.application.service;

import com.poly.promotion.data.access.jpaentity.MonitoringDataEntity;

import java.util.List;

/**
 * Service for synchronizing local monitoring data with external monitoring service.
 * Provides retry mechanism and data recovery for failed syncs.
 *
 * @author System
 * @since 1.0.0
 */
public interface MonitoringDataSyncService {

    /**
     * Synchronizes all unsynced monitoring data with the external service.
     * This method is called periodically to ensure data consistency.
     *
     * @return the number of successfully synced records
     */
    int syncUnsyncedData();

    /**
     * Synchronizes monitoring data of a specific type.
     *
     * @param dataType the type of monitoring data to sync
     * @return the number of successfully synced records
     */
    int syncDataByType(MonitoringDataEntity.MonitoringDataType dataType);

    /**
     * Synchronizes a specific monitoring data record.
     *
     * @param monitoringData the monitoring data to sync
     * @return true if sync was successful, false otherwise
     */
    boolean syncSingleRecord(MonitoringDataEntity monitoringData);

    /**
     * Gets statistics about sync operations.
     *
     * @return sync statistics
     */
    SyncStatistics getSyncStatistics();

    /**
     * Manually triggers a sync operation.
     * Useful for administrative purposes or testing.
     */
    void triggerManualSync();

    /**
     * Gets the list of failed sync records for manual review.
     *
     * @return list of failed sync records
     */
    List<MonitoringDataEntity> getFailedSyncRecords();

    /**
     * Retries failed sync operations.
     *
     * @param maxRetries the maximum number of retries
     * @return the number of successfully retried records
     */
    int retryFailedSyncs(int maxRetries);

    /**
     * Statistics for sync operations.
     */
    record SyncStatistics(
        long totalRecords,
        long syncedRecords,
        long unsyncedRecords,
        long failedRecords,
        long lastSyncTime,
        int averageSyncTimeMs
    ) {}
}
