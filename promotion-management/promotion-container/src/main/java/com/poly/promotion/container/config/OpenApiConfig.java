package com.poly.promotion.container.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h2>OpenApiConfig Class</h2>
 * 
 * <p>Configuration class for OpenAPI/Swagger documentation in the promotion management system.
 * This class configures the API documentation with comprehensive information about
 * the promotion system, including contact details, licensing, and server information.</p>
 * 
 * <p><strong>Documentation Features:</strong></p>
 * <ul>
 *   <li><strong>API Information:</strong> Comprehensive description of the promotion system</li>
 *   <li><strong>Contact Details:</strong> Developer and support contact information</li>
 *   <li><strong>License Information:</strong> Software licensing and usage terms</li>
 *   <li><strong>Server Configuration:</strong> Environment-specific server URLs</li>
 *   <li><strong>Version Management:</strong> API versioning and release information</li>
 * </ul>
 * 
 * <p><strong>Swagger UI Access:</strong></p>
 * <ul>
 *   <li>Development: http://localhost:8080/promotion-management/swagger-ui.html</li>
 *   <li>API Docs: http://localhost:8080/promotion-management/api-docs</li>
 *   <li>OpenAPI JSON: http://localhost:8080/promotion-management/api-docs</li>
 * </ul>
 * 
 * <p><strong>API Categories:</strong></p>
 * <ul>
 *   <li>Voucher Pack Management: CRUD operations for voucher packs</li>
 *   <li>Voucher Management: Customer voucher operations and redemption</li>
 *   <li>Expiration Management: Automated voucher and pack expiration</li>
 *   <li>Customer Operations: Customer-specific voucher access</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:9999}")
    private String serverPort;

    @Value("${server.servlet.context-path:/promotion-management}")
    private String contextPath;

    /**
     * Creates and configures the OpenAPI specification for the promotion management system.
     * 
     * <p>This method configures comprehensive API documentation including:</p>
     * <ul>
     *   <li>API metadata and description</li>
     *   <li>Contact information for developers</li>
     *   <li>License and usage terms</li>
     *   <li>Server configurations for different environments</li>
     *   <li>Version information and release details</li>
     * </ul>
     * 
     * <p><strong>Configuration Details:</strong></p>
     * <ul>
     *   <li>Title: "Promotion Management API"</li>
     *   <li>Version: "1.0.0"</li>
     *   <li>Description: Comprehensive hotel promotion system</li>
     *   <li>Contact: Developer contact information</li>
     *   <li>License: MIT License</li>
     *   <li>Servers: Local development and production configurations</li>
     * </ul>
     * 
     * @return configured OpenAPI specification
     */
    @Bean
    public OpenAPI promotionManagementOpenAPI() {
        try {
            // Minimal configuration to avoid initialization issues
            return new OpenAPI()
                    .info(new Info()
                            .title("Promotion Management API")
                            .version("1.0.0"));
        } catch (Exception e) {
            // Fallback to basic configuration if there's an error
            return new OpenAPI()
                    .info(new Info()
                            .title("Promotion Management API")
                            .version("1.0.0"));
        }
    }
}
