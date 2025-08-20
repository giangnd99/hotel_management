package com.poly.promotion.application.service;

/**
 * Service for cleaning up old monitoring data.
 * Manages data retention policies and storage optimization.
 *
 * @author System
 * @since 1.0.0
 */
public interface MonitoringDataCleanupService {

    /**
     * Cleans up old monitoring data based on retention policies.
     * This method is scheduled to run periodically.
     *
     * @return the number of deleted records
     */
    int cleanupOldData();

    /**
     * Cleans up old synced monitoring data.
     * Synced data can be deleted sooner than unsynced data.
     *
     * @return the number of deleted records
     */
    int cleanupOldSyncedData();

    /**
     * Cleans up old monitoring data of a specific type.
     *
     * @param dataType the type of monitoring data to clean up
     * @return the number of deleted records
     */
    int cleanupDataByType(String dataType);

    /**
     * Gets cleanup statistics.
     *
     * @return cleanup statistics
     */
    CleanupStatistics getCleanupStatistics();

    /**
     * Manually triggers cleanup operation.
     * Useful for administrative purposes or testing.
     */
    void triggerManualCleanup();

    /**
     * Statistics for cleanup operations.
     */
    record CleanupStatistics(
        long totalRecordsBefore,
        long totalRecordsAfter,
        long deletedRecords,
        long cleanupTimeMs,
        String lastCleanupTime
    ) {}
}
