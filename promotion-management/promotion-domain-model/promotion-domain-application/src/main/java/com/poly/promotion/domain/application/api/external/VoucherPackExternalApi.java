package com.poly.promotion.domain.application.api.external;

import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;

import java.util.List;

/**
 * <h2>VoucherPackExternalApi Interface</h2>
 * 
 * <p>External API interface for voucher pack operations that are accessible to external systems,
 * third-party integrations, and public-facing applications. This interface provides a controlled
 * subset of voucher pack functionality for external consumption.</p>
 * 
 * <p><strong>API Design Principles:</strong></p>
 * <ul>
 *   <li><strong>Read-Only Operations:</strong> External APIs typically provide read access only</li>
 *   <li><strong>Data Sanitization:</strong> Sensitive information is filtered out</li>
 *   <li><strong>Rate Limiting:</strong> External calls may be subject to rate limiting</li>
 *   <li><strong>Authentication:</strong> External access requires proper authentication</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This interface is typically used by:</p>
 * <ul>
 *   <li>Customer-facing applications (mobile apps, web portals)</li>
 *   <li>Third-party booking systems</li>
 *   <li>Partner hotel applications</li>
 *   <li>Public promotional displays</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>External APIs should not expose internal business logic</li>
 *   <li>Response data should be sanitized to prevent information leakage</li>
 *   <li>Access should be properly authenticated and authorized</li>
 *   <li>Rate limiting should be implemented to prevent abuse</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackExternalResponse
 * @see com.poly.promotion.domain.application.api.external.impl.VoucherPackExternalApiImpl
 */
public interface VoucherPackExternalApi {
    
    /**
     * Retrieves all available voucher packs for external consumption.
     * 
     * <p>This method returns voucher packs that are currently available for customer
     * redemption. The response includes only the information necessary for external
     * systems and customers to make redemption decisions.</p>
     * 
     * <p><strong>Response Filtering:</strong></p>
     * <ul>
     *   <li>Only PUBLISHED packs are returned</li>
     *   <li>Internal business logic is excluded</li>
     *   <li>Sensitive information is filtered out</li>
     *   <li>Data is formatted for external consumption</li>
     * </ul>
     * 
     * <p><strong>Performance Considerations:</strong></p>
     * <ul>
     *   <li>Results may be cached for performance</li>
     *   <li>Large result sets may be paginated</li>
     *   <li>Database queries are optimized for read operations</li>
     * </ul>
     * 
     * @return a list of available voucher packs formatted for external consumption
     */
    List<VoucherPackExternalResponse> getAllAvailableVoucherPacks();
}
