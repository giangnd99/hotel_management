package com.poly.promotion.application.batch.scheduler;

import com.poly.promotion.application.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Scheduler for Spring Batch jobs.
 * Handles scheduling of expiration management and monitoring data sync jobs.
 * Jobs run nightly at midnight and optionally on service startup.
 *
 * @author System
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BatchJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job expirationManagementJob;
    private final Job monitoringDataSyncJob;
    private final MonitoringService monitoringService;
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Runs the expiration management job nightly at midnight (00:00).
     * This job checks and marks expired vouchers and voucher packs.
     */
    @Scheduled(cron = "${batch.job.expiration.cron:0 0 0 * * ?}")
    public void scheduleExpirationManagementJob() {
        log.info("🕛 Scheduling expiration management job at {}", LocalDateTime.now().format(FORMATTER));
        runJob(expirationManagementJob, "expirationManagementJob");
    }

    /**
     * Runs the monitoring data sync job nightly at midnight (00:00).
     * This job sends accumulated monitoring data to the external service.
     */
    @Scheduled(cron = "${batch.job.monitoring.cron:0 0 0 * * ?}")
    public void scheduleMonitoringDataSyncJob() {
        log.info("🕛 Scheduling monitoring data sync job at {}", LocalDateTime.now().format(FORMATTER));
        runJob(monitoringDataSyncJob, "monitoringDataSyncJob");
    }

    /**
     * Runs both jobs when the application starts up.
     * This ensures that any pending expiration checks are performed.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void runJobsOnStartup() {
        log.info("🚀 Application ready - running batch jobs on startup");
        
        // Run expiration management job on startup
        log.info("🔄 Running expiration management job on startup...");
        runJob(expirationManagementJob, "expirationManagementJob-startup");
        
        // Run monitoring data sync job on startup
        log.info("🔄 Running monitoring data sync job on startup...");
        runJob(monitoringDataSyncJob, "monitoringDataSyncJob-startup");
    }

    /**
     * Executes a batch job with the given name.
     *
     * @param job the job to execute
     * @param jobName the name of the job for logging
     */
    private void runJob(Job job, String jobName) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .addString("jobName", jobName)
                    .addString("executionTime", LocalDateTime.now().format(FORMATTER))
                    .toJobParameters();

            log.info("🚀 Launching job: {} with parameters: {}", jobName, jobParameters);
            
            var execution = jobLauncher.run(job, jobParameters);
            
            log.info("✅ Job {} completed successfully with status: {}", 
                    jobName, execution.getStatus());
            
            // Send audit log to monitoring service
            String auditEntry = String.format(
                "SCHEDULED_JOB: %s completed successfully - Status: %s - Timestamp: %s",
                jobName,
                execution.getStatus(),
                LocalDateTime.now().format(FORMATTER)
            );
            monitoringService.sendAuditLogEntry(auditEntry);
            
        } catch (JobExecutionAlreadyRunningException e) {
            log.warn("⚠️ Job {} is already running", jobName);
        } catch (JobRestartException e) {
            log.error("❌ Job {} restart failed", jobName, e);
            sendJobErrorToMonitoring(jobName, "Job restart failed", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.warn("⚠️ Job {} instance already completed", jobName);
        } catch (JobParametersInvalidException e) {
            log.error("❌ Job {} parameters invalid", jobName, e);
            sendJobErrorToMonitoring(jobName, "Job parameters invalid", e);
        } catch (Exception e) {
            log.error("❌ Job {} execution failed", jobName, e);
            sendJobErrorToMonitoring(jobName, "Job execution failed", e);
        }
    }

    /**
     * Sends job error information to the monitoring service.
     *
     * @param jobName the name of the failed job
     * @param errorMessage the error message
     * @param exception the exception that occurred
     */
    private void sendJobErrorToMonitoring(String jobName, String errorMessage, Exception exception) {
        try {
            String errorInfo = String.format(
                "SCHEDULED_JOB_ERROR: %s - Error: %s - Exception: %s - Timestamp: %s",
                jobName,
                errorMessage,
                exception.getMessage(),
                LocalDateTime.now().format(FORMATTER)
            );
            monitoringService.sendErrorInfo(errorInfo);
        } catch (Exception e) {
            log.warn("Failed to send job error to monitoring service: {}", e.getMessage());
        }
    }
}
