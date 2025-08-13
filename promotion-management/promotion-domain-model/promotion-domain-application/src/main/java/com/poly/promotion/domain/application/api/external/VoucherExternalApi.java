package com.poly.promotion.domain.application.api.external;

import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;

import java.util.List;

/**
 * <h2>VoucherExternalApi Interface</h2>
 * 
 * <p>External API interface for voucher operations that are accessible to external systems,
 * third-party integrations, and customer-facing applications. This interface provides a controlled
 * subset of voucher functionality for external consumption.</p>
 * 
 * <p><strong>API Design Principles:</strong></p>
 * <ul>
 *   <li><strong>Customer-Centric:</strong> Focuses on customer voucher management</li>
 *   <li><strong>Security-First:</strong> Ensures customer data privacy and security</li>
 *   <li><strong>Performance-Optimized:</strong> Designed for efficient external consumption</li>
 *   <li><strong>Rate-Limited:</strong> External access is controlled to prevent abuse</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This interface is typically used by:</p>
 * <ul>
 *   <li>Customer mobile applications</li>
 *   <li>Customer web portals</li>
 *   <li>Third-party loyalty applications</li>
 *   <li>Partner booking systems</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>Customer authentication is required for all operations</li>
 *   <li>Vouchers can only be accessed by their rightful owners</li>
 *   <li>Internal business logic is not exposed</li>
 *   <li>Response data is filtered to prevent information leakage</li>
 * </ul>
 * 
 * <p><strong>Data Privacy:</strong></p>
 * <ul>
 *   <li>Customer-specific data is isolated</li>
 *   <li>Internal system details are hidden</li>
 *   <li>Audit trails are maintained for security</li>
 *   <li>Access patterns are monitored for suspicious activity</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherExternalResponse
 * @see VoucherRedeemRequest
 * @see com.poly.promotion.domain.application.api.external.impl.VoucherExternalApiImpl
 */
public interface VoucherExternalApi {
    
    /**
     * Retrieves all vouchers for a specific customer, formatted for external consumption.
     * 
     * <p>This method allows customers to view their voucher collection through external
     * applications. The method ensures data privacy and security by filtering sensitive
     * information and maintaining customer data isolation.</p>
     * 
     * <p><strong>Authentication Requirements:</strong></p>
     * <ul>
     *   <li>Customer must be properly authenticated</li>
     *   <li>Customer ID must match the authenticated user</li>
     *   <li>Access is logged for security auditing</li>
     * </ul>
     * 
     * <p><strong>Response Filtering:</strong></p>
     * <ul>
     *   <li>Only customer's own vouchers are returned</li>
     *   <li>Internal business logic is excluded</li>
     *   <li>Sensitive information is filtered out</li>
     *   <li>Data is formatted for external consumption</li>
     * </ul>
     * 
     * <p><strong>Performance Considerations:</strong></p>
     * <ul>
     *   <li>Results may be cached for performance</li>
     *   <li>Large result sets may be paginated</li>
     *   <li>Database queries are optimized for customer-specific access</li>
     * </ul>
     * 
     * @param customerId the unique identifier of the customer
     * @return a list of vouchers belonging to the customer, formatted for external consumption
     * @throws IllegalArgumentException if customerId is null or invalid
     * @throws SecurityException if the customer is not authenticated or authorized
     */
    List<VoucherExternalResponse> getCustomerVouchers(String customerId);
    
    /**
     * Allows a customer to redeem a voucher from a voucher pack using their loyalty points.
     * 
     * <p>This method enables customers to exchange their accumulated loyalty points for
     * vouchers from available voucher packs. The redemption process includes validation
     * of customer eligibility, voucher pack availability, and loyalty point balance.</p>
     * 
     * <p><strong>Redemption Process:</strong></p>
     * <ol>
     *   <li>Validates customer authentication and authorization</li>
     *   <li>Checks voucher pack availability and status</li>
     *   <li>Verifies customer loyalty point balance</li>
     *   <li>Creates a new voucher for the customer</li>
     *   <li>Deducts loyalty points from customer account</li>
     *   <li>Returns the redeemed voucher details</li>
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
     * <p><strong>Response Data:</strong></p>
     * <ul>
     *   <li>Redeemed voucher details</li>
     *   <li>Voucher code for usage</li>
     *   <li>Validity period information</li>
     *   <li>Discount amount and type</li>
     *   <li>Redemption timestamp</li>
     * </ul>
     * 
     * @param request the voucher redemption request containing customer and voucher pack information
     * @return the redeemed voucher details formatted for external consumption
     * @throws IllegalArgumentException if the request data is invalid
     * @throws SecurityException if the customer is not authenticated or authorized
     * @throws com.poly.promotion.domain.core.exception.PromotionDomainException if business rules are violated
     */
    VoucherExternalResponse redeemVoucherFromPack(VoucherRedeemRequest request);
}
