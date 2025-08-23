package com.poly.promotion.application.batch;

import com.poly.promotion.application.batch.listener.JobCompletionNotificationListener;
import com.poly.promotion.application.batch.step.ExpirationManagementStep;
import com.poly.promotion.application.batch.step.MonitoringDataSyncStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch configuration for scheduled jobs.
 * Configures batch jobs for expiration management and monitoring data synchronization.
 *
 * @author System
 * @since 1.0.0
 */
@Configuration
@EnableBatchProcessing
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final JobCompletionNotificationListener jobCompletionNotificationListener;
    private final ExpirationManagementStep expirationManagementStep;
    private final MonitoringDataSyncStep monitoringDataSyncStep;

    /**
     * Configures the expiration management job.
     * This job runs nightly to check and mark expired vouchers and voucher packs.
     *
     * @return the configured job
     */
    @Bean
    public Job expirationManagementJob() {
        return new JobBuilder("expirationManagementJob", jobRepository)
                .listener(jobCompletionNotificationListener)
                .start(expirationManagementStepBean())
                .build();
    }

    /**
     * Configures the monitoring data synchronization job.
     * This job runs nightly to send accumulated monitoring data to the external service.
     *
     * @return the configured job
     */
    @Bean
    public Job monitoringDataSyncJob() {
        return new JobBuilder("monitoringDataSyncJob", jobRepository)
                .listener(jobCompletionNotificationListener)
                .start(monitoringDataSyncStepBean())
                .build();
    }

    /**
     * Creates a Step from the ExpirationManagementStep tasklet.
     *
     * @return the configured step
     */
    @Bean
    public Step expirationManagementStepBean() {
        return new StepBuilder("expirationManagementStep", jobRepository)
                .tasklet(expirationManagementStep, transactionManager)
                .build();
    }

    /**
     * Creates a Step from the MonitoringDataSyncStep tasklet.
     *
     * @return the configured step
     */
    @Bean
    public Step monitoringDataSyncStepBean() {
        return new StepBuilder("monitoringDataSyncStep", jobRepository)
                .tasklet(monitoringDataSyncStep, transactionManager)
                .build();
    }
}
