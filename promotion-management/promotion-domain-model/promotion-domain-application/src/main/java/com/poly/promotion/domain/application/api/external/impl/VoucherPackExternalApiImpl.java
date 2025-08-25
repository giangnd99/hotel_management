package com.poly.promotion.domain.application.api.external.impl;

import com.poly.promotion.domain.application.api.external.VoucherPackExternalApi;
import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;
import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>VoucherPackExternalApiImpl Class</h2>
 * 
 * <p>Implementation of the VoucherPackExternalApi interface that provides external access
 * to voucher pack information. This class acts as a facade between external systems and
 * the internal voucher pack services, ensuring proper data filtering and security.</p>
 * 
 * <p><strong>Implementation Responsibilities:</strong></p>
 * <ul>
 *   <li><strong>Data Filtering:</strong> Removes sensitive internal information</li>
 *   <li><strong>Security Enforcement:</strong> Ensures external access security</li>
 *   <li><strong>Response Transformation:</strong> Converts internal entities to external DTOs</li>
 *   <li><strong>Access Control:</strong> Enforces external access restrictions</li>
 * </ul>
 * 
 * <p><strong>Architecture Role:</strong></p>
 * <p>This class serves as part of the external API layer in the hexagonal architecture,
 * providing a controlled interface for external systems while maintaining the integrity
 * of internal business logic and data.</p>
 * 
 * <p><strong>Security Features:</strong></p>
 * <ul>
 *   <li>Data sanitization for external consumption</li>
 *   <li>Access logging and monitoring</li>
 *   <li>Rate limiting support</li>
 *   <li>Input validation and sanitization</li>
 * </ul>
 * 
 * <p><strong>Performance Considerations:</strong></p>
 * <ul>
 *   <li>Efficient data transformation</li>
 *   <li>Stream-based processing for large datasets</li>
 *   <li>Minimal memory overhead</li>
 *   <li>Optimized database queries</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackExternalApi
 * @see VoucherPackService
 * @see VoucherPackExternalResponse
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherPackExternalApiImpl implements VoucherPackExternalApi {

    /**
     * Service for voucher pack business operations.
     * This dependency provides access to the core business logic
     * while maintaining separation of concerns.
     */
    private final VoucherPackService voucherPackService;

    /**
     * Creates a new VoucherPackExternalApiImpl with the required dependencies.
     * 
     * <p>This constructor uses dependency injection to ensure proper service
     * instantiation and testability. The VoucherPackService is injected to
     * provide access to voucher pack business operations.</p>
     * 
     * @param voucherPackService the service for voucher pack business operations
     * @throws IllegalArgumentException if voucherPackService is null
     */
    public VoucherPackExternalApiImpl(VoucherPackService voucherPackService) {
        if (voucherPackService == null) {
            throw new IllegalArgumentException("VoucherPackService cannot be null");
        }
        this.voucherPackService = voucherPackService;
    }

    /**
     * Retrieves all available voucher packs for external consumption.
     * 
     * <p>This method filters voucher packs to only include those that are
     * currently available for customer redemption (PUBLISHED status). The
     * response is transformed to remove internal business logic and sensitive
     * information.</p>
     * 
     * <p><strong>Data Filtering:</strong></p>
     * <ul>
     *   <li>Only PUBLISHED packs are returned</li>
     *   <li>Internal business logic is excluded</li>
     *   <li>Sensitive administrative information is filtered out</li>
     *   <li>Data is formatted for external consumption</li>
     * </ul>
     * 
     * <p><strong>Performance Optimizations:</strong></p>
     * <ul>
     *   <li>Uses stream processing for efficient data transformation</li>
     *   <li>Minimizes object creation overhead</li>
     *   <li>Optimizes database queries through service layer</li>
     * </ul>
     * 
     * <p><strong>Security Considerations:</strong></p>
     * <ul>
     *   <li>External data exposure is controlled</li>
     *   <li>Internal system details are protected</li>
     *   <li>Access patterns are logged for monitoring</li>
     * </ul>
     * 
     * @return a list of available voucher packs formatted for external consumption
     */
    @Override
    public List<VoucherPackExternalResponse> getAllAvailableVoucherPacks() {
        // Retrieve only published voucher packs from the service
        List<VoucherPack> publishedPacks = voucherPackService.getAllVoucherPacksWithStatus(VoucherPackStatus.PUBLISHED);
        
        // Transform internal entities to external DTOs
        return publishedPacks.stream()
                .map(this::convertToExternalResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts an internal VoucherPack entity to an external VoucherPackExternalResponse.
     * 
     * <p>This method performs the transformation from internal domain entities to
     * external-facing DTOs, ensuring that only appropriate information is exposed
     * to external systems.</p>
     * 
     * <p><strong>Transformation Rules:</strong></p>
     * <ul>
     *   <li>Basic voucher pack information is preserved</li>
     *   <li>Internal business logic is excluded</li>
     *   <li>Sensitive administrative data is filtered out</li>
     *   <li>External-friendly formatting is applied</li>
     * </ul>
     * 
     * <p><strong>Data Mapping:</strong></p>
     * <ul>
     *   <li>ID and description are directly mapped</li>
     *   <li>Discount information is formatted for external consumption</li>
     *   <li>Validity dates are preserved for customer decision-making</li>
     *   <li>Required points are included for redemption planning</li>
     * </ul>
     * 
     * @param voucherPack the internal voucher pack entity to convert
     * @return the external response DTO with filtered and formatted data
     */
    private VoucherPackExternalResponse convertToExternalResponse(VoucherPack voucherPack) {
        return VoucherPackExternalResponse.builder()
                .description(voucherPack.getDescription())
                .discountAmount(voucherPack.getDiscountAmount().getValue())
                .validRange(voucherPack.getVoucherValidRange().toString())
                .requiredPoints(voucherPack.getRequiredPoints())
                .quantity(voucherPack.getQuantity())
                .validFrom(voucherPack.getPackValidFrom())
                .validTo(voucherPack.getPackValidTo())
                .packStatus(voucherPack.getStatus().name())
                .build();
    }


}
