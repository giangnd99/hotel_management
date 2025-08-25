package com.poly.promotion.domain.application.api.internal.impl;

import com.poly.promotion.domain.application.api.internal.VoucherPackInternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackCreateRequest;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackUpdateRequest;
import com.poly.promotion.domain.application.dto.response.internal.VoucherPackInternalResponse;
import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>VoucherPackInternalApiImpl Class</h2>
 * 
 * <p>Implementation of the VoucherPackInternalApi interface that provides internal access
 * to voucher pack functionality for administrative and system operations. This class
 * serves as the primary interface for internal voucher pack management.</p>
 * 
 * <p><strong>Implementation Responsibilities:</strong></p>
 * <ul>
 *   <li><strong>Administrative Operations:</strong> Full voucher pack lifecycle management</li>
 *   <li><strong>Business Logic Integration:</strong> Coordinates with domain services</li>
 *   <li><strong>Data Transformation:</strong> Converts between DTOs and domain entities</li>
 *   <li><strong>Validation and Security:</strong> Enforces business rules and access control</li>
 * </ul>
 * 
 * <p><strong>Architecture Role:</strong></p>
 * <p>This class serves as part of the internal API layer in the hexagonal architecture,
 * providing administrative access to voucher pack functionality while maintaining
 * proper separation of concerns and business rule enforcement.</p>
 * 
 * <p><strong>Administrative Features:</strong></p>
 * <ul>
 *   <li>Complete voucher pack creation and configuration</li>
 *   <li>Full update capabilities for pending packs</li>
 *   <li>Comprehensive administrative data access</li>
 *   <li>Business rule enforcement and validation</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>Internal access control and authentication</li>
 *   <li>Role-based permission enforcement</li>
 *   <li>Audit trail maintenance</li>
 *   <li>Business rule compliance validation</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackInternalApi
 * @see VoucherPackService
 * @see VoucherPackCreateRequest
 * @see VoucherPackUpdateRequest
 * @see VoucherPackInternalResponse
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherPackInternalApiImpl implements VoucherPackInternalApi {

    /**
     * Service for voucher pack business operations.
     * This dependency provides access to the core business logic
     * while maintaining separation of concerns.
     */
    private final VoucherPackService voucherPackService;

    /**
     * Creates a new VoucherPackInternalApiImpl with the required dependencies.
     * 
     * <p>This constructor uses dependency injection to ensure proper service
     * instantiation and testability. The VoucherPackService is injected to
     * provide access to voucher pack business operations.</p>
     * 
     * @param voucherPackService the service for voucher pack business operations
     * @throws IllegalArgumentException if voucherPackService is null
     */
    public VoucherPackInternalApiImpl(VoucherPackService voucherPackService) {
        if (voucherPackService == null) {
            throw new IllegalArgumentException("VoucherPackService cannot be null");
        }
        this.voucherPackService = voucherPackService;
    }

    /**
     * Creates a new voucher pack in the system.
     * 
     * <p>This method allows administrators to create new voucher packs with full
     * configuration options. The method coordinates with the domain service to
     * enforce business rules and create the voucher pack entity.</p>
     * 
     * <p><strong>Creation Process:</strong></p>
     * <ol>
     *   <li>Validates the creation request data</li>
     *   <li>Converts the request to a domain entity</li>
     *   <li>Delegates to the domain service for business logic</li>
     *   <li>Transforms the result to internal response format</li>
     * </ol>
     * 
     * <p><strong>Business Rules Enforced:</strong></p>
     * <ul>
     *   <li>Voucher pack data validation</li>
     *   <li>Business rule compliance</li>
     *   <li>Data integrity checks</li>
     *   <li>Creation audit logging</li>
     * </ul>
     * 
     * @param request the voucher pack creation request containing all necessary data
     * @return the created voucher pack with internal response format
     * @throws IllegalArgumentException if the request data is invalid
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if business rules are violated
     */
    @Override
    public VoucherPackInternalResponse createVoucherPack(VoucherPackCreateRequest request) {
        // Convert the request to a domain entity
        VoucherPack voucherPack = request.toEntity();
        
        // Delegate to the domain service for business logic
        VoucherPack createdPack = voucherPackService.createVoucherPack(voucherPack, "system");
        
        // Transform the result to internal response format
        return convertToInternalResponse(createdPack);
    }

    /**
     * Updates an existing voucher pack in the system.
     * 
     * <p>This method allows administrators to modify existing voucher packs.
     * Only packs in PENDING status can be updated to maintain data integrity.
     * The method coordinates with the domain service to enforce business rules.</p>
     * 
     * <p><strong>Update Process:</strong></p>
     * <ol>
     *   <li>Validates the update request data</li>
     *   <li>Retrieves the existing voucher pack</li>
     *   <li>Applies the updates through the domain service</li>
     *   <li>Transforms the result to internal response format</li>
     * </ol>
     * 
     * <p><strong>Update Restrictions:</strong></p>
     * <ul>
     *   <li>Only PENDING packs can be modified</li>
     *   <li>Published packs cannot be modified</li>
     *   <li>Closed or expired packs cannot be modified</li>
     * </ul>
     * 
     * @param voucherPackId the ID of the voucher pack to update
     * @param request the update request containing the new data
     * @return the updated voucher pack with internal response format
     * @throws IllegalArgumentException if the request data is invalid
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if the pack cannot be updated
     */
    @Override
    public VoucherPackInternalResponse updateVoucherPack(Long voucherPackId, VoucherPackUpdateRequest request) {        
        // Apply the updates from the request
        VoucherPack updatedPack = request.toEntity();
        
        // Delegate to the domain service for business logic
        VoucherPack result = voucherPackService.updatePendingVoucherPack(voucherPackId, updatedPack, "system");
        
        // Transform the result to internal response format
        return convertToInternalResponse(result);
    }

    /**
     * Retrieves all voucher packs with optional status filtering.
     * 
     * <p>This method provides administrators with access to all voucher packs
     * in the system, with optional filtering by status for easier management.
     * The method coordinates with the domain service to retrieve the data.</p>
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
    @Override
    public List<VoucherPackInternalResponse> getAllVoucherPacks(String... status) {
        // Convert status strings to enum values if provided
        VoucherPackStatus[] statusEnums = null;
        if (status != null && status.length > 0) {
            statusEnums = new VoucherPackStatus[status.length];
            for (int i = 0; i < status.length; i++) {
                statusEnums[i] = VoucherPackStatus.fromString(status[i]);
            }
        }
        
        // Retrieve voucher packs from the domain service
        List<VoucherPack> voucherPacks = voucherPackService.getAllVoucherPacksWithStatus(statusEnums);
        
        // Transform the results to internal response format
        return voucherPacks.stream()
                .map(this::convertToInternalResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a VoucherPack domain entity to a VoucherPackInternalResponse.
     * 
     * <p>This method performs the transformation from domain entities to
     * internal response DTOs, ensuring that all necessary information is
     * available for administrative operations.</p>
     * 
     * <p><strong>Transformation Rules:</strong></p>
     * <ul>
     *   <li>All voucher pack information is preserved</li>
     *   <li>Internal business logic is maintained</li>
     *   <li>Administrative data is included</li>
     *   <li>Internal-friendly formatting is applied</li>
     * </ul>
     * 
     * <p><strong>Data Mapping:</strong></p>
     * <ul>
     *   <li>ID and description are directly mapped</li>
     *   <li>Discount information is preserved</li>
     *   <li>Validity dates and status are included</li>
     *   <li>Administrative timestamps are maintained</li>
     * </ul>
     * 
     * @param voucherPack the voucher pack domain entity to convert
     * @return the internal response DTO with complete administrative data
     */
    private VoucherPackInternalResponse convertToInternalResponse(VoucherPack voucherPack) {
        return VoucherPackInternalResponse.builder()
                .id(voucherPack.getId() != null ? voucherPack.getId().getValue() : null)
                .description(voucherPack.getDescription())
                .discountAmount(voucherPack.getDiscountAmount() != null ? voucherPack.getDiscountAmount().getValue() : null)
                .validRange(voucherPack.getVoucherValidRange() != null ? voucherPack.getVoucherValidRange().toString() : null)
                .requiredPoints(voucherPack.getRequiredPoints())
                .quantity(voucherPack.getQuantity())
                .validFrom(voucherPack.getPackValidFrom())
                .validTo(voucherPack.getPackValidTo())
                .createdAt(voucherPack.getCreatedAt())
                .createdBy(voucherPack.getCreatedBy())
                .updatedAt(voucherPack.getUpdatedAt())
                .updatedBy(voucherPack.getUpdatedBy())
                .status(voucherPack.getStatus() != null ? voucherPack.getStatus().name() : null)
                .build();
    }

    @Override
    public VoucherPackInternalResponse getVoucherPackById(Long voucherPackId) {
        VoucherPack voucherPack = voucherPackService.getVoucherPackById(voucherPackId);
        return convertToInternalResponse(voucherPack);
    }

    @Override
    public void closeVoucherPack(Long voucherPackId) {
        voucherPackService.closeVoucherPack(voucherPackId);
    }

    @Override
    public VoucherPackInternalResponse publishVoucherPack(Long voucherPackId) {
        VoucherPack publishedPack = voucherPackService.publishVoucherPack(voucherPackId);
        return convertToInternalResponse(publishedPack);
    }
}
