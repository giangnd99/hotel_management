package com.poly.promotion.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.promotion.application.client.MonitoringServiceClient;
import com.poly.promotion.application.dto.monitoring.MethodExecutionMetrics;
import com.poly.promotion.application.dto.monitoring.PerformanceMetrics;
import com.poly.promotion.application.dto.monitoring.SystemHealthMetrics;
import com.poly.promotion.application.service.MonitoringDataService;
import com.poly.promotion.application.service.MonitoringDataSyncService;
import com.poly.promotion.data.access.jpaentity.MonitoringDataEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of MonitoringDataSyncService.
 * Provides retry mechanism and data recovery for failed syncs.
 *
 * @author System
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MonitoringDataSyncServiceImpl implements MonitoringDataSyncService {

    private final MonitoringServiceClient monitoringServiceClient;
    private final MonitoringDataService monitoringDataService;
    private final ObjectMapper objectMapper;

    @Value("${monitoring.sync.max-retries:3}")
    private int maxRetries;



    private final AtomicLong totalSyncTime = new AtomicLong(0);
    private final AtomicLong syncCount = new AtomicLong(0);

    @Override
    @Scheduled(fixedRateString = "${monitoring.sync.interval:60000}") // Default: every minute
    public int syncUnsyncedData() {
        log.info("🔄 Starting sync of unsynced monitoring data...");
        
        List<MonitoringDataEntity> unsyncedData = monitoringDataService.getUnsyncedData();
        if (unsyncedData.isEmpty()) {
            log.debug("No unsynced data to sync");
            return 0;
        }

        log.info("Found {} unsynced records to sync", unsyncedData.size());
        
        int successCount = 0;
        long startTime = System.currentTimeMillis();
        
        for (MonitoringDataEntity data : unsyncedData) {
            try {
                if (syncSingleRecord(data)) {
                    successCount++;
                }
            } catch (Exception e) {
                log.error("Failed to sync record {}: {}", data.getId(), e.getMessage());
            }
        }
        
        long syncTime = System.currentTimeMillis() - startTime;
        updateSyncStatistics(syncTime);
        
        log.info("✅ Sync completed: {}/{} records synced successfully in {}ms", 
                successCount, unsyncedData.size(), syncTime);
        
        return successCount;
    }

    @Override
    public int syncDataByType(MonitoringDataEntity.MonitoringDataType dataType) {
        log.info("🔄 Starting sync of {} data...", dataType);
        
        List<MonitoringDataEntity> unsyncedData = monitoringDataService.getUnsyncedDataByType(dataType);
        if (unsyncedData.isEmpty()) {
            log.debug("No unsynced {} data to sync", dataType);
            return 0;
        }

        int successCount = 0;
        long startTime = System.currentTimeMillis();
        
        for (MonitoringDataEntity data : unsyncedData) {
            try {
                if (syncSingleRecord(data)) {
                    successCount++;
                }
            } catch (Exception e) {
                log.error("Failed to sync {} record {}: {}", dataType, data.getId(), e.getMessage());
            }
        }
        
        long syncTime = System.currentTimeMillis() - startTime;
        updateSyncStatistics(syncTime);
        
        log.info("✅ {} sync completed: {}/{} records synced successfully in {}ms", 
                dataType, successCount, unsyncedData.size(), syncTime);
        
        return successCount;
    }

    @Override
    public boolean syncSingleRecord(MonitoringDataEntity monitoringData) {
        try {            
            // Check if we've exceeded max retries
            if (monitoringData.getSyncAttempts() >= maxRetries) {
                log.warn("Record {} has exceeded max retries ({}), skipping sync", 
                        monitoringData.getId(), maxRetries);
                return false;
            }
            
            boolean syncSuccess = false;
            
            switch (monitoringData.getDataType()) {
                case METHOD_EXECUTION_METRICS:
                    syncSuccess = syncMethodExecutionMetrics(monitoringData);
                    break;
                case PERFORMANCE_METRICS:
                    syncSuccess = syncPerformanceMetrics(monitoringData);
                    break;
                case SYSTEM_HEALTH_METRICS:
                    syncSuccess = syncSystemHealthMetrics(monitoringData);
                    break;
                case AUDIT_LOG_ENTRY:
                    syncSuccess = syncAuditLogEntry(monitoringData);
                    break;
                case ERROR_INFO:
                    syncSuccess = syncErrorInfo(monitoringData);
                    break;
                default:
                    log.warn("Unknown data type: {}", monitoringData.getDataType());
                    return false;
            }
            
            if (syncSuccess) {
                monitoringDataService.markAsSynced(monitoringData.getId());
                log.debug("Successfully synced record {} to external service", monitoringData.getId());
                return true;
            } else {
                // Update sync attempt info
                monitoringDataService.updateSyncAttemptInfo(monitoringData.getId(), 
                        "Sync failed for unknown reason");
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error syncing record {}: {}", monitoringData.getId(), e.getMessage());
            monitoringDataService.updateSyncAttemptInfo(monitoringData.getId(), e.getMessage());
            return false;
        }
    }

    @Override
    public SyncStatistics getSyncStatistics() {
        long totalRecords = monitoringDataService.countByDataType(null);
        long syncedRecords = totalRecords - monitoringDataService.countUnsyncedData();
        long unsyncedRecords = monitoringDataService.countUnsyncedData();
        
        long lastSyncTime = this.totalSyncTime.get();
        int averageSyncTimeMs = syncCount.get() > 0 ? 
                (int) (this.totalSyncTime.get() / syncCount.get()) : 0;
        
        return new SyncStatistics(
                totalRecords,
                syncedRecords,
                unsyncedRecords,
                0, // Failed records count would need additional tracking
                lastSyncTime,
                averageSyncTimeMs
        );
    }

    @Override
    public void triggerManualSync() {
        log.info("🔄 Manual sync triggered");
        syncUnsyncedData();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonitoringDataEntity> getFailedSyncRecords() {
        // This would need a custom query to find records with sync errors
        // For now, return unsynced data
        return monitoringDataService.getUnsyncedData();
    }

    @Override
    public int retryFailedSyncs(int maxRetries) {
        log.info("🔄 Retrying failed syncs with max retries: {}", maxRetries);
        
        List<MonitoringDataEntity> failedRecords = getFailedSyncRecords();
        int successCount = 0;
        
        for (MonitoringDataEntity record : failedRecords) {
            if (record.getSyncAttempts() < maxRetries) {
                try {
                    if (syncSingleRecord(record)) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.error("Failed to retry sync for record {}: {}", record.getId(), e.getMessage());
                }
            }
        }
        
        log.info("✅ Retry completed: {}/{} records synced successfully", 
                successCount, failedRecords.size());
        
        return successCount;
    }

    // Private helper methods for syncing different data types
    private boolean syncMethodExecutionMetrics(MonitoringDataEntity data) throws JsonProcessingException {
        MethodExecutionMetrics metrics = objectMapper.readValue(data.getDataContent(), MethodExecutionMetrics.class);
        var response = monitoringServiceClient.sendMethodExecutionMetrics(metrics);
        return response.getStatusCode().is2xxSuccessful();
    }

    private boolean syncPerformanceMetrics(MonitoringDataEntity data) throws JsonProcessingException {
        PerformanceMetrics metrics = objectMapper.readValue(data.getDataContent(), PerformanceMetrics.class);
        var response = monitoringServiceClient.sendPerformanceMetrics(metrics);
        return response.getStatusCode().is2xxSuccessful();
    }

    private boolean syncSystemHealthMetrics(MonitoringDataEntity data) throws JsonProcessingException {
        SystemHealthMetrics metrics = objectMapper.readValue(data.getDataContent(), SystemHealthMetrics.class);
        var response = monitoringServiceClient.sendSystemHealthMetrics(metrics);
        return response.getStatusCode().is2xxSuccessful();
    }

    private boolean syncAuditLogEntry(MonitoringDataEntity data) {
        var response = monitoringServiceClient.sendAuditLogEntry(data.getDataContent());
        return response.getStatusCode().is2xxSuccessful();
    }

    private boolean syncErrorInfo(MonitoringDataEntity data) {
        var response = monitoringServiceClient.sendErrorInfo(data.getDataContent());
        return response.getStatusCode().is2xxSuccessful();
    }

    private void updateSyncStatistics(long syncTime) {
        totalSyncTime.addAndGet(syncTime);
        syncCount.incrementAndGet();
    }
}
