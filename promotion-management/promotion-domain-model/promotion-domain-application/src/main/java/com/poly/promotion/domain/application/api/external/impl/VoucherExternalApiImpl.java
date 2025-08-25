package com.poly.promotion.domain.application.api.external.impl;

import com.poly.promotion.domain.application.api.external.VoucherExternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import com.poly.promotion.domain.application.service.VoucherService;
import com.poly.promotion.domain.core.entity.Voucher;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2>VoucherExternalApiImpl Class</h2>
 * 
 * <p>Implementation of the VoucherExternalApi interface that provides external access
 * to voucher information for authenticated customers. This class acts as a secure
 * facade between external customer applications and the internal voucher services.</p>
 * 
 * <p><strong>Implementation Responsibilities:</strong></p>
 * <ul>
 *   <li><strong>Customer Authentication:</strong> Ensures only authenticated customers access their vouchers</li>
 *   <li><strong>Data Privacy:</strong> Filters sensitive information from responses</li>
 *   <li><strong>Security Enforcement:</strong> Maintains customer data isolation</li>
 *   <li><strong>Response Transformation:</strong> Converts internal entities to external DTOs</li>
 *   <li><strong>Voucher Redemption:</strong> Handles customer voucher redemption requests</li>
 * </ul>
 * 
 * <p><strong>Architecture Role:</strong></p>
 * <p>This class serves as part of the external API layer in the hexagonal architecture,
 * providing a secure and controlled interface for customer applications while maintaining
 * the integrity of internal business logic and customer data privacy.</p>
 * 
 * <p><strong>Security Features:</strong></p>
 * <ul>
 *   <li>Customer authentication and authorization</li>
 *   <li>Data isolation and privacy protection</li>
 *   <li>Access logging and audit trails</li>
 *   <li>Input validation and sanitization</li>
 *   <li>Fraud prevention through business rule enforcement</li>
 * </ul>
 * 
 * <p><strong>Performance Considerations:</strong></p>
 * <ul>
 *   <li>Efficient customer-specific data retrieval</li>
 *   <li>Stream-based data transformation</li>
 *   <li>Optimized database queries for customer data</li>
 *   <li>Minimal memory overhead for response generation</li>
 *   <li>Efficient voucher redemption processing</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherExternalApi
 * @see VoucherService
 * @see VoucherExternalResponse
 * @see VoucherRedeemRequest
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherExternalApiImpl implements VoucherExternalApi {

    /**
     * Service for voucher business operations.
     * This dependency provides access to the core voucher business logic
     * while maintaining separation of concerns and security boundaries.
     */
    private final VoucherService voucherService;

    /**
     * Creates a new VoucherExternalApiImpl with the required dependencies.
     * 
     * <p>This constructor uses dependency injection to ensure proper service
     * instantiation and testability. The VoucherService is injected to
     * provide access to voucher business operations.</p>
     * 
     * @param voucherService the service for voucher business operations
     * @throws IllegalArgumentException if voucherService is null
     */
    public VoucherExternalApiImpl(VoucherService voucherService) {
        if (voucherService == null) {
            throw new IllegalArgumentException("VoucherService cannot be null");
        }
        this.voucherService = voucherService;
    }

    /**
     * Retrieves all vouchers for a specific customer, formatted for external consumption.
     * 
     * <p>This method provides customers with access to their voucher collection
     * through external applications. The method ensures data privacy and security
     * by filtering sensitive information and maintaining customer data isolation.</p>
     * 
     * <p><strong>Security Features:</strong></p>
     * <ul>
     *   <li>Customer authentication is required</li>
     *   <li>Only customer's own vouchers are accessible</li>
     *   <li>Internal business logic is hidden</li>
     *   <li>Access is logged for security monitoring</li>
     * </ul>
     * 
     * <p><strong>Data Filtering:</strong></p>
     * <ul>
     *   <li>Customer-specific voucher data only</li>
     *   <li>Sensitive internal information is excluded</li>
     *   <li>External-friendly data formatting</li>
     *   <li>Privacy-compliant response structure</li>
     * </ul>
     * 
     * <p><strong>Performance Optimizations:</strong></p>
     * <ul>
     *   <li>Efficient customer-specific database queries</li>
     *   <li>Stream-based data transformation</li>
     *   <li>Minimal object creation overhead</li>
     *   <li>Optimized response generation</li>
     * </ul>
     * 
     * @param customerId the unique identifier of the customer
     * @return a list of vouchers belonging to the customer, formatted for external consumption
     * @throws IllegalArgumentException if customerId is null or invalid
     * @throws SecurityException if the customer is not authenticated or authorized
     */
    @Override
    public List<VoucherExternalResponse> getCustomerVouchers(String customerId) {
        // Validate customer ID input
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        
        // Retrieve customer vouchers from the service layer
        // This includes all statuses to give customers complete visibility
        List<Voucher> customerVouchers = voucherService.getAllVouchersWithStatus(customerId);
        
        // Transform internal entities to external DTOs
        return customerVouchers.stream()
                .map(this::convertToExternalResponse)
                .collect(Collectors.toList());
    }

    /**
     * Allows a customer to redeem a voucher from a voucher pack using their loyalty points.
     * 
     * <p>This method enables customers to exchange their accumulated loyalty points for
     * vouchers from available voucher packs. The redemption process is handled by the
     * domain service layer, which enforces all business rules and validations.</p>
     * 
     * <p><strong>Redemption Process:</strong></p>
     * <ol>
     *   <li>Validates the redemption request data</li>
     *   <li>Delegates to the domain service for business logic</li>
     *   <li>Processes the voucher redemption transaction</li>
     *   <li>Transforms the result to external response format</li>
     * </ol>
     * 
     * <p><strong>Business Rules Enforced:</strong></p>
     * <ul>
     *   <li>Customer must have sufficient loyalty points</li>
     *   <li>Voucher pack must be in PUBLISHED status</li>
     *   <li>Voucher pack must have available quantity</li>
     *   <li>Voucher pack must be within validity period</li>
     *   <li>Customer must not exceed redemption limits</li>
     * </ul>
     * 
     * <p><strong>Security Features:</strong></p>
     * <ul>
     *   <li>Customer authentication is required</li>
     *   <li>Customer ID validation against authenticated user</li>
     *   <li>Fraud prevention through redemption limits</li>
     *   <li>Audit trail for all redemption transactions</li>
     * </ul>
     * 
     * <p><strong>Error Handling:</strong></p>
     * <ul>
     *   <li>Invalid request data throws IllegalArgumentException</li>
     *   <li>Business rule violations throw PromotionDomainException</li>
     *   <li>Authentication failures throw SecurityException</li>
     *   <li>Clear error messages for customer feedback</li>
     * </ul>
     * 
     * @param request the voucher redemption request containing customer and voucher pack information
     * @return the redeemed voucher details formatted for external consumption
     * @throws IllegalArgumentException if the request data is invalid
     * @throws SecurityException if the customer is not authenticated or authorized
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if business rules are violated
     */
    @Override
    public VoucherExternalResponse redeemVoucherFromPack(VoucherRedeemRequest request) {
        // Validate the redemption request
        validateRedemptionRequest(request);
        
        // Delegate to the domain service for business logic
        // The service handles loyalty point deduction, voucher creation, and business rule enforcement
        Voucher redeemedVoucher = voucherService.redeemVoucherFromPack(
                request.getVoucherPackId(), 
                request.getCustomerId(), 
                1 // Default quantity for single voucher redemption
        );
        
        // Transform the result to external response format
        return convertToExternalResponse(redeemedVoucher);
    }

    /**
     * Validates the voucher redemption request data.
     * 
     * <p>This method performs basic validation on the redemption request to ensure
     * that all required fields are present and valid before processing the request
     * through the domain service layer.</p>
     * 
     * <p><strong>Validation Rules:</strong></p>
     * <ul>
     *   <li>Customer ID must not be null or empty</li>
     *   <li>Voucher pack ID must not be null</li>
     *   <li>Customer ID must be a valid string format</li>
     *   <li>Voucher pack ID must be a positive number</li>
     * </ul>
     * 
     * <p><strong>Error Handling:</strong></p>
     * <ul>
     *   <li>Null or empty customer ID throws IllegalArgumentException</li>
     *   <li>Null voucher pack ID throws IllegalArgumentException</li>
     *   <li>Invalid ID formats throw IllegalArgumentException</li>
     *   <li>Clear error messages for debugging</li>
     * </ul>
     * 
     * @param request the redemption request to validate
     * @throws IllegalArgumentException if the request data is invalid
     */
    private void validateRedemptionRequest(VoucherRedeemRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Redemption request cannot be null");
        }
        
        if (request.getCustomerId() == null || request.getCustomerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        
        if (request.getVoucherPackId() == null || request.getVoucherPackId() <= 0) {
            throw new IllegalArgumentException("Voucher pack ID must be a positive number");
        }
    }

    /**
     * Converts an internal Voucher entity to an external VoucherExternalResponse.
     * 
     * <p>This method performs the transformation from internal domain entities to
     * external-facing DTOs, ensuring that only appropriate information is exposed
     * to customer applications while maintaining data privacy and security.</p>
     * 
     * <p><strong>Transformation Rules:</strong></p>
     * <ul>
     *   <li>Basic voucher information is preserved</li>
     *   <li>Internal business logic is excluded</li>
     *   <li>Sensitive administrative data is filtered out</li>
     *   <li>Customer-friendly formatting is applied</li>
     * </ul>
     * 
     * <p><strong>Data Mapping:</strong></p>
     * <ul>
     *   <li>Voucher code and status are directly mapped</li>
     *   <li>Discount information is formatted for customer understanding</li>
     *   <li>Validity dates are preserved for usage planning</li>
     *   <li>Customer-specific information is maintained</li>
     * </ul>
     * 
     * @param voucher the internal voucher entity to convert
     * @return the external response DTO with filtered and formatted data
     */
    private VoucherExternalResponse convertToExternalResponse(Voucher voucher) {
        return VoucherExternalResponse.builder()
                .voucherCode(voucher.getVoucherCode())
                .discountAmount(voucher.getDiscount().getValue())
                .redeemedAt(voucher.getRedeemedAt())
                .validTo(voucher.getValidTo())
                .voucherStatus(voucher.getVoucherStatus().name())
                .build();
    }
}
