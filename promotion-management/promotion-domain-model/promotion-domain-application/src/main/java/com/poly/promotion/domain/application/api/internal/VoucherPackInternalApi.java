package com.poly.promotion.domain.application.api.internal;

import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackCreateRequest;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackUpdateRequest;
import com.poly.promotion.domain.application.dto.response.internal.VoucherPackInternalResponse;
import java.util.List;

/**
 * <h2>VoucherPackInternalApi Interface</h2>
 * 
 * <p>Internal API interface for voucher pack operations that are accessible only to internal
 * system components, administrative interfaces, and authorized internal services. This interface
 * provides full access to voucher pack functionality for internal system operations.</p>
 * 
 * <p><strong>API Design Principles:</strong></p>
 * <ul>
 *   <li><strong>Full Access:</strong> Provides complete voucher pack management capabilities</li>
 *   <li><strong>Internal Use Only:</strong> Not exposed to external systems or customers</li>
 *   <li><strong>Business Logic:</strong> Includes internal business rules and validations</li>
 *   <li><strong>Administrative Control:</strong> Designed for administrative and system operations</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This interface is typically used by:</p>
 * <ul>
 *   <li>Administrative web interfaces</li>
 *   <li>Internal system services</li>
 *   <li>Automated business processes</li>
 *   <li>System maintenance and monitoring tools</li>
 *   <li>Internal reporting and analytics systems</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>Access is restricted to authenticated internal users</li>
 *   <li>Role-based access control is enforced</li>
 *   <li>All operations are logged for audit purposes</li>
 *   <li>Internal business logic is fully accessible</li>
 * </ul>
 * 
 * <p><strong>Business Operations:</strong></p>
 * <ul>
 *   <li>Complete voucher pack lifecycle management</li>
 *   <li>Administrative operations and overrides</li>
 *   <li>System maintenance and bulk operations</li>
 *   <li>Internal reporting and data access</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackCreateRequest
 * @see VoucherPackUpdateRequest
 * @see VoucherPackInternalResponse
 * @see com.poly.promotion.domain.application.api.internal.impl.VoucherPackInternalApiImpl
 */
public interface VoucherPackInternalApi {
    
    /**
     * Creates a new voucher pack in the system.
     * 
     * <p>This method allows administrators to create new voucher packs with full
     * configuration options. The method enforces all business rules and validations
     * during the creation process.</p>
     * 
     * <p><strong>Business Rules Enforced:</strong></p>
     * <ul>
     *   <li>Voucher pack data validation</li>
     *   <li>Business rule compliance</li>
     *   <li>Data integrity checks</li>
     *   <li>Audit trail creation</li>
     * </ul>
     * 
     * <p><strong>Administrative Features:</strong></p>
     * <ul>
     *   <li>Full configuration options</li>
     *   <li>Business rule overrides (if authorized)</li>
     *   <li>Detailed error reporting</li>
     *   <li>Creation audit logging</li>
     * </ul>
     * 
     * @param request the voucher pack creation request containing all necessary data
     * @return the created voucher pack with internal response format
     * @throws IllegalArgumentException if the request data is invalid
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if business rules are violated
     */
    VoucherPackInternalResponse createVoucherPack(VoucherPackCreateRequest request);
    
    /**
     * Updates an existing voucher pack in the system.
     * 
     * <p>This method allows administrators to modify existing voucher packs.
     * Only packs in PENDING status can be updated to maintain data integrity.</p>
     * 
     * <p><strong>Update Restrictions:</strong></p>
     * <ul>
     *   <li>Only PENDING packs can be modified</li>
     *   <li>Published packs cannot be modified</li>
     *   <li>Closed or expired packs cannot be modified</li>
     * </ul>
     * 
     * <p><strong>Validation Features:</strong></p>
     * <ul>
     *   <li>Business rule validation</li>
     *   <li>Data integrity checks</li>
     *   <li>Status transition validation</li>
     *   <li>Update audit logging</li>
     * </ul>
     * 
     * @param voucherPackId the ID of the voucher pack to update
     * @param request the update request containing the new data
     * @return the updated voucher pack with internal response format
     * @throws IllegalArgumentException if the request data is invalid
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the pack cannot be updated
     */
    VoucherPackInternalResponse updateVoucherPack(Long voucherPackId, VoucherPackUpdateRequest request);
    
    /**
     * Retrieves all voucher packs with optional status filtering.
     * 
     * <p>This method provides administrators with access to all voucher packs
     * in the system, with optional filtering by status for easier management.</p>
     * 
     * <p><strong>Access Features:</strong></p>
     * <ul>
     *   <li>Complete voucher pack data access</li>
     *   <li>Status-based filtering options</li>
     *   <li>Internal business logic visibility</li>
     *   <li>Administrative data access</li>
     * </ul>
     * 
     * <p><strong>Performance Considerations:</strong></p>
     * <ul>
     *   <li>Results may be paginated for large datasets</li>
     *   <li>Database queries are optimized for administrative access</li>
     *   <li>Caching may be disabled for real-time administrative data</li>
     * </ul>
     * 
     * @param status optional status values to filter by (if none provided, returns all packs)
     * @return a list of voucher packs with internal response format
     */
    List<VoucherPackInternalResponse> getAllVoucherPacks(String... status);

    /**
     * Retrieves a specific voucher pack by ID for administrative purposes.
     * 
     * <p>This method provides administrators with detailed information about
     * a specific voucher pack, including all internal data and status information.</p>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to retrieve
     * @return the voucher pack with internal response format
     * @throws IllegalArgumentException if the voucher pack ID is invalid
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the voucher pack is not found
     */
    VoucherPackInternalResponse getVoucherPackById(Long voucherPackId);

    /**
     * Closes a voucher pack, making it unavailable for customer redemption.
     * 
     * <p>This method allows administrators to manually close voucher packs
     * that are no longer needed or have been discontinued.</p>
     * 
     * <p><strong>Closure Rules:</strong></p>
     * <ul>
     *   <li>Only PENDING or PUBLISHED packs can be closed</li>
     *   <li>CLOSED and EXPIRED packs cannot be closed again</li>
     *   <li>Existing vouchers remain valid until their individual expiration</li>
     * </ul>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to close
     * @throws IllegalArgumentException if the voucher pack ID is invalid
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the voucher pack cannot be closed
     */
    void closeVoucherPack(Long voucherPackId);

    /**
     * Publishes a PENDING voucher pack, making it available for customer redemption.
     * 
     * <p>This method allows administrators to manually activate voucher packs
     * that were created with future start dates or were kept in PENDING status.</p>
     * 
     * <p><strong>Publication Rules:</strong></p>
     * <ul>
     *   <li>Only PENDING packs can be published</li>
     *   <li>Pack must have valid dates and configuration</li>
     *   <li>Publication is immediate regardless of packValidFrom date</li>
     * </ul>
     * 
     * @param voucherPackId the unique identifier of the voucher pack to publish
     * @return the published voucher pack with internal response format
     * @throws IllegalArgumentException if the voucher pack ID is invalid
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the voucher pack cannot be published
     */
    VoucherPackInternalResponse publishVoucherPack(Long voucherPackId);
}
