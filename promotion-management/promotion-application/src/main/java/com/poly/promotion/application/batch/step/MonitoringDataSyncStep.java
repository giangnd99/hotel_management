package com.poly.promotion.application.batch.step;

import com.poly.promotion.application.service.MonitoringDataSyncService;
import com.poly.promotion.application.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Spring Batch step for synchronizing monitoring data with external monitoring service.
 * This step runs as part of the nightly monitoring data sync job.
 *
 * @author System
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MonitoringDataSyncStep implements Tasklet {

    private final MonitoringService monitoringService;
    private final MonitoringDataSyncService monitoringDataSyncService;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("🔄 Starting monitoring data sync step at {}", startTime.format(FORMATTER));
        
        try {
            // Trigger sync of local monitoring data with external service
            log.info("📊 Syncing local monitoring data with external service...");
            
            int syncedCount = monitoringDataSyncService.syncUnsyncedData();
            
            // Send audit summary
            log.info("📝 Sending audit summary to monitoring service...");
            String auditSummary = String.format(
                "BATCH_JOB: Monitoring data sync completed - Records synced: %d, Timestamp: %s",
                syncedCount,
                startTime.format(FORMATTER)
            );
            monitoringService.sendAuditLogEntry(auditSummary);
            
            // Step contribution updated automatically by Spring Batch
            
            LocalDateTime endTime = LocalDateTime.now();
            long duration = java.time.Duration.between(startTime, endTime).toMillis();
            log.info("✅ Monitoring data sync step completed in {}ms - {} records synced", 
                    duration, syncedCount);
            
            return RepeatStatus.FINISHED;
            
        } catch (Exception e) {
            log.error("❌ Monitoring data sync step failed", e);
            
            // Send error info to monitoring service
            String errorInfo = String.format(
                "BATCH_JOB_ERROR: Monitoring data sync failed - Exception: %s - Timestamp: %s",
                e.getMessage(),
                startTime.format(FORMATTER)
            );
            monitoringService.sendErrorInfo(errorInfo);
            
            throw e;
        }
    }


}
