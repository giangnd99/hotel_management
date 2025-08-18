package com.poly.promotion.container.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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

    @Value("${server.port:8080}")
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
        return new OpenAPI()
                .info(new Info()
                        .title("Promotion Management API")
                        .description("""
                                <h2>Hotel Promotion Management System</h2>
                                
                                <p>Comprehensive API for managing hotel promotions, voucher packs, and customer voucher operations.</p>
                                
                                <h3>System Overview</h3>
                                <p>The Promotion Management System provides a complete solution for hotel marketing and customer loyalty programs, including:</p>
                                <ul>
                                    <li><strong>Voucher Pack Management:</strong> Create, update, and manage promotional voucher packs</li>
                                    <li><strong>Customer Voucher Operations:</strong> Redeem vouchers using loyalty points</li>
                                    <li><strong>Expiration Management:</strong> Automated handling of expired vouchers and packs</li>
                                    <li><strong>Business Rule Enforcement:</strong> Validation of promotional rules and constraints</li>
                                </ul>
                                
                                <h3>Key Features</h3>
                                <ul>
                                    <li><strong>Clean Architecture:</strong> Built using Domain-Driven Design principles</li>
                                    <li><strong>Hexagonal Architecture:</strong> Separation of concerns with clear boundaries</li>
                                    <li><strong>Business Rule Validation:</strong> Comprehensive validation of promotional rules</li>
                                    <li><strong>Event-Driven Design:</strong> Domain events for system integration</li>
                                    <li><strong>Audit Trail:</strong> Complete tracking of all promotional activities</li>
                                </ul>
                                
                                <h3>API Categories</h3>
                                <ul>
                                    <li><strong>Voucher Packs:</strong> <code>/api/v1/voucher-packs</code> - Manage promotional voucher packs</li>
                                    <li><strong>Vouchers:</strong> <code>/api/v1/vouchers</code> - Handle individual voucher operations</li>
                                    <li><strong>Customer Operations:</strong> <code>/api/v1/vouchers/customer/{id}</code> - Customer-specific voucher access</li>
                                    <li><strong>Redemption:</strong> <code>/api/v1/vouchers/redeem</code> - Process voucher redemptions</li>
                                </ul>
                                
                                <h3>Authentication & Security</h3>
                                <p>All endpoints require proper authentication and authorization. Customer endpoints are restricted to access only the authenticated customer's data.</p>
                                
                                <h3>Rate Limiting</h3>
                                <p>API endpoints are subject to rate limiting to ensure system stability and prevent abuse.</p>
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hotel Management Development Team")
                                .email("dev@hotelmanagement.com")
                                .url("https://hotelmanagement.com/developers"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort + contextPath)
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.hotelmanagement.com" + contextPath)
                                .description("Production Server"),
                        new Server()
                                .url("https://staging-api.hotelmanagement.com" + contextPath)
                                .description("Staging Server")
                ));
    }
}
