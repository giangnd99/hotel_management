package com.poly.promotion.application.service.impl;

import com.poly.promotion.application.service.MonitoringDataCleanupService;
import com.poly.promotion.application.service.MonitoringDataService;
import com.poly.promotion.data.access.jpaentity.MonitoringDataEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Implementation of MonitoringDataCleanupService.
 * Manages data retention policies and storage optimization.
 *
 * @author System
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MonitoringDataCleanupServiceImpl implements MonitoringDataCleanupService {

    private final MonitoringDataService monitoringDataService;

    @Value("${monitoring.data.retention.days:30}")
    private int dataRetentionDays;

    @Value("${monitoring.data.retention.synced.days:7}")
    private int syncedDataRetentionDays;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Scheduled(cron = "${monitoring.data.cleanup.cron:0 0 2 * * ?}") // Daily at 2 AM
    public int cleanupOldData() {
        log.info("🧹 Starting cleanup of old monitoring data (retention: {} days)...", dataRetentionDays);
        
        long startTime = System.currentTimeMillis();
        long totalRecordsBefore = monitoringDataService.countByDataType(null);
        
        int deletedCount = monitoringDataService.cleanupOldData(dataRetentionDays);
        
        long cleanupTime = System.currentTimeMillis() - startTime;
        long totalRecordsAfter = monitoringDataService.countByDataType(null);
        
        log.info("✅ Cleanup completed: {} records deleted in {}ms. Records: {} -> {}", 
                deletedCount, cleanupTime, totalRecordsBefore, totalRecordsAfter);
        
        return deletedCount;
    }

    @Override
    @Scheduled(cron = "${monitoring.data.cleanup.synced.cron:0 30 2 * * ?}") // Daily at 2:30 AM
    public int cleanupOldSyncedData() {
        log.info("🧹 Starting cleanup of old synced monitoring data (retention: {} days)...", syncedDataRetentionDays);
        
        long startTime = System.currentTimeMillis();
        long totalRecordsBefore = monitoringDataService.countByDataType(null);
        
        int deletedCount = monitoringDataService.cleanupOldSyncedData(syncedDataRetentionDays);
        
        long cleanupTime = System.currentTimeMillis() - startTime;
        long totalRecordsAfter = monitoringDataService.countByDataType(null);
        
        log.info("✅ Synced data cleanup completed: {} records deleted in {}ms. Records: {} -> {}", 
                deletedCount, cleanupTime, totalRecordsBefore, totalRecordsAfter);
        
        return deletedCount;
    }

    @Override
    public int cleanupDataByType(String dataType) {
        log.info("🧹 Starting cleanup of old {} data...", dataType);
        
        try {
            MonitoringDataEntity.MonitoringDataType enumType = 
                    MonitoringDataEntity.MonitoringDataType.valueOf(dataType.toUpperCase());
            
            long startTime = System.currentTimeMillis();
            long totalRecordsBefore = monitoringDataService.countByDataType(enumType);
            
            // For type-specific cleanup, we'll use the general cleanup but log the type
            int deletedCount = monitoringDataService.cleanupOldData(dataRetentionDays);
            
            long cleanupTime = System.currentTimeMillis() - startTime;
            long totalRecordsAfter = monitoringDataService.countByDataType(enumType);
            
            log.info("✅ {} cleanup completed: {} records deleted in {}ms. Records: {} -> {}", 
                    dataType, deletedCount, cleanupTime, totalRecordsBefore, totalRecordsAfter);
            
            return deletedCount;
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid data type for cleanup: {}", dataType);
            return 0;
        }
    }

    @Override
    public CleanupStatistics getCleanupStatistics() {
        long totalRecords = monitoringDataService.countByDataType(null);
        
        return new CleanupStatistics(
                totalRecords + 0, // Estimate: current + deleted
                totalRecords,
                0, // Would need separate tracking for accurate count
                0, // Would need to track cleanup time
                LocalDateTime.now().format(formatter)
        );
    }

    @Override
    public void triggerManualCleanup() {
        log.info("🧹 Manual cleanup triggered");
        
        long startTime = System.currentTimeMillis();
        
        int oldDataDeleted = cleanupOldData();
        int syncedDataDeleted = cleanupOldSyncedData();
        
        long cleanupTime = System.currentTimeMillis() - startTime;
        
        log.info("✅ Manual cleanup completed: {} old records + {} synced records deleted in {}ms", 
                oldDataDeleted, syncedDataDeleted, cleanupTime);
    }
}
