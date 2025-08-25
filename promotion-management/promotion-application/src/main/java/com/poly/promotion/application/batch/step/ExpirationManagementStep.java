package com.poly.promotion.application.batch.step;

import com.poly.promotion.application.service.MonitoringService;
import com.poly.promotion.domain.application.service.ExpirationManagementService;
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
 * Spring Batch step for managing expired vouchers and voucher packs.
 * This step runs as part of the nightly expiration management job.
 *
 * @author System
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExpirationManagementStep implements Tasklet {

    private final ExpirationManagementService expirationManagementService;
    private final MonitoringService monitoringService;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("🔄 Starting expiration management step at {}", startTime.format(FORMATTER));
        
        try {
            // Perform comprehensive expiration check
            var summary = expirationManagementService.performComprehensiveExpirationCheck();
            
            // Log results
            log.info("📊 Expiration management completed successfully:");
            log.info("  - Voucher packs processed: {}", summary.expiredVoucherPacks());
            log.info("  - Vouchers processed: {}", summary.expiredVouchers());
            log.info("  - Total expired items: {}", 
                    summary.expiredVoucherPacks() + summary.expiredVouchers());
            
            // Send audit log to monitoring service
            String auditEntry = String.format(
                "BATCH_JOB: Expiration management completed - VoucherPacks: %d, Vouchers: %d, ProcessingTime: %dms",
                summary.expiredVoucherPacks(),
                summary.expiredVouchers(),
                summary.totalProcessingTimeMs()
            );
            monitoringService.sendAuditLogEntry(auditEntry);
            
            // Step contribution updated automatically by Spring Batch
            
            LocalDateTime endTime = LocalDateTime.now();
            long duration = java.time.Duration.between(startTime, endTime).toMillis();
            log.info("✅ Expiration management step completed in {}ms", duration);
            
            return RepeatStatus.FINISHED;
            
        } catch (Exception e) {
            log.error("❌ Expiration management step failed", e);
            
            // Send error info to monitoring service
            String errorInfo = String.format(
                "BATCH_JOB_ERROR: Expiration management failed - Exception: %s - Timestamp: %s",
                e.getMessage(),
                startTime.format(FORMATTER)
            );
            monitoringService.sendErrorInfo(errorInfo);
            
            throw e;
        }
    }
}
