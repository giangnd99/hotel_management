package com.poly.promotion.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <h2>PromotionManagementApplication Class</h2>
 * 
 * <p>Main Spring Boot application class for the promotion management system.
 * This class serves as the entry point for the application and configures
 * the Spring Boot application context with all necessary features.</p>
 * 
 * <p><strong>Application Features:</strong></p>
 * <ul>
 *   <li><strong>Spring Boot Auto-configuration:</strong> Automatic configuration of Spring components</li>
 *   <li><strong>Component Scanning:</strong> Scans for Spring components across all modules</li>
 *   <li><strong>Entity Scanning:</strong> Scans for JPA entities in the domain model</li>
 *   <li><strong>Repository Scanning:</strong> Scans for Spring Data repositories</li>

 *   <li><strong>Transaction Management:</strong> Enables declarative transaction management</li>
 * </ul>
 * 
 * <p><strong>Module Integration:</strong></p>
 * <ul>
 *   <li>promotion-application: REST controllers and web layer</li>
 *   <li>promotion-domain-application: Domain services and business logic</li>
 *   <li>promotion-data-access: Data persistence and repository layer</li>
 *   <li>promotion-container: Configuration and main application</li>
 * </ul>
 * 
 * <p><strong>Architecture Benefits:</strong></p>
 * <ul>
 *   <li>Clean separation of concerns between modules</li>
 *   <li>Domain-driven design principles implementation</li>
 *   <li>Hexagonal architecture with clear boundaries</li>
 *   <li>Dependency injection and inversion of control</li>
 *   <li>Comprehensive API documentation with Swagger</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.poly.promotion.container.config",
    "com.poly.promotion.application",
    "com.poly.promotion.domain.application",
    "com.poly.promotion.data.access"
})
@EntityScan(basePackages = {
    "com.poly.promotion.domain.core.entity",
    "com.poly.promotion.data.access.jpaentity"
})
@EnableJpaRepositories(basePackages = {
    "com.poly.promotion.data.access.jparepository"
})

@EnableTransactionManagement
public class PromotionManagementApplication {

    /**
     * Main method that starts the Spring Boot application.
     * 
     * <p>This method initializes the Spring application context and starts
     * the embedded web server. The application will be available at the
     * configured port with the specified context path.</p>
     * 
     * <p><strong>Startup Process:</strong></p>
     * <ol>
     *   <li>Initialize Spring application context</li>
     *   <li>Scan for components and configurations</li>
     *   <li>Initialize database connections</li>
     *   <li>Start embedded web server</li>
     *   <li>Register REST endpoints</li>
    
     * </ol>
     * 
     * <p><strong>Access URLs:</strong></p>
     * <ul>
     *   <li>Application: http://localhost:9999/promotion-management</li>
     *   <li>Swagger UI: http://localhost:9999/promotion-management/swagger-ui.html</li>
     *   <li>API Docs: http://localhost:9999/promotion-management/api-docs</li>
     *   <li>Health Check: http://localhost:9999/promotion-management/actuator/health</li>
     * </ul>
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(PromotionManagementApplication.class, args);
    }
}
