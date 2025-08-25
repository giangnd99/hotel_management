package com.poly.promotion.application.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Listener for batch job completion events.
 * Logs job execution results and provides notifications.
 *
 * @author System
 * @since 1.0.0
 */
@Component
@Slf4j
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        LocalDateTime startTime = jobExecution.getStartTime();
        
        log.info("🚀 Starting batch job: {} at {}", jobName, 
                startTime != null ? startTime.format(FORMATTER) : "N/A");
        log.info("Job parameters: {}", jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        BatchStatus status = jobExecution.getStatus();
        LocalDateTime endTime = jobExecution.getEndTime();
        LocalDateTime startTime = jobExecution.getStartTime();
        
        long duration = 0;
        if (startTime != null && endTime != null) {
            duration = java.time.Duration.between(startTime, endTime).toMillis();
        }
        
        log.info("✅ Batch job completed: {} - Status: {} - Duration: {}ms", 
                jobName, status, duration);
        
        if (status == BatchStatus.COMPLETED) {
            log.info("🎉 Job '{}' completed successfully in {}ms", jobName, duration);
        } else if (status == BatchStatus.FAILED) {
            log.error("❌ Job '{}' failed after {}ms", jobName, duration);
            jobExecution.getAllFailureExceptions().forEach(ex -> 
                log.error("Failure exception: {}", ex.getMessage(), ex));
        } else if (status == BatchStatus.STOPPED) {
            log.warn("⏹️ Job '{}' was stopped after {}ms", jobName, duration);
        }
        
        // Log job execution summary
        log.info("Job execution summary for '{}':", jobName);
        log.info("  - Start time: {}", 
                startTime != null ? startTime.format(FORMATTER) : "N/A");
        log.info("  - End time: {}", 
                endTime != null ? endTime.format(FORMATTER) : "N/A");
        log.info("  - Duration: {}ms", duration);
        log.info("  - Exit code: {}", jobExecution.getExitStatus().getExitCode());
        log.info("  - Exit description: {}", jobExecution.getExitStatus().getExitDescription());
    }
}
