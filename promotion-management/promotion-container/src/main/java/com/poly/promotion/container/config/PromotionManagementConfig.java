package com.poly.promotion.container.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <h2>PromotionManagementConfig Class</h2>
 * 
 * <p>Main configuration class for the promotion management system.
 * This class configures component scanning, entity scanning, repository scanning,
 * and enables various Spring features required for the promotion system.</p>
 * 
 * <p><strong>Configuration Features:</strong></p>
 * <ul>
 *   <li><strong>Component Scanning:</strong> Scans for Spring components across modules</li>
 *   <li><strong>Entity Scanning:</strong> Scans for JPA entities in the domain model</li>
 *   <li><strong>Repository Scanning:</strong> Scans for Spring Data repositories</li>
 *   <li><strong>Scheduling:</strong> Enables scheduled tasks for expiration management</li>
 *   <li><strong>Transaction Management:</strong> Enables declarative transaction management</li>
 * </ul>
 * 
 * <p><strong>Module Coverage:</strong></p>
 * <ul>
 *   <li>promotion-application: REST controllers and application services</li>
 *   <li>promotion-domain-application: Domain application services and APIs</li>
 *   <li>promotion-data-access: Data access layer and repositories</li>
 *   <li>promotion-container: Configuration and main application</li>
 * </ul>
 * 
 * <p><strong>Spring Features Enabled:</strong></p>
 * <ul>
 *   <li>Component scanning for dependency injection</li>
 *   <li>JPA entity scanning for database operations</li>
 *   <li>Repository scanning for data access</li>
 *   <li>Scheduled task execution</li>
 *   <li>Declarative transaction management</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 */
@Configuration
@ComponentScan(basePackages = {
    "com.poly.promotion.application",
    "com.poly.promotion.domain.application",
    "com.poly.promotion.data.access"
})
@EntityScan(basePackages = {
    "com.poly.promotion.domain.core.entity"
})
@EnableJpaRepositories(basePackages = {
    "com.poly.promotion"
})
@EnableScheduling
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.poly.promotion.application.client")
public class PromotionManagementConfig {

    /**
     * Default constructor for the configuration class.
     * 
     * <p>This constructor is used by Spring to instantiate the configuration
     * class. No additional configuration is required in the constructor.</p>
     */
    public PromotionManagementConfig() {
        // Default constructor - Spring will handle instantiation
    }
}
