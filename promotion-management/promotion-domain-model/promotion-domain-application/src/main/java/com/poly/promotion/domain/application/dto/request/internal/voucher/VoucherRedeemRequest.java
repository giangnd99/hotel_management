package com.poly.promotion.domain.application.dto.request.internal.voucher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * <h2>VoucherRedeemRequest Class</h2>
 * 
 * <p>Data Transfer Object (DTO) for customer voucher redemption requests in the promotion system.
 * This class encapsulates the data required for a customer to redeem a voucher from a voucher pack
 * using their accumulated loyalty points.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>Data Transfer:</strong> Carries redemption request data from external APIs to domain services</li>
 *   <li><strong>Customer Redemption:</strong> Enables customers to exchange loyalty points for vouchers</li>
 *   <li><strong>Request Validation:</strong> Provides structure for input validation and processing</li>
 *   <li><strong>Business Process:</strong> Initiates the voucher redemption workflow</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This DTO is typically used by:</p>
 * <ul>
 *   <li>Customer mobile applications for voucher redemption</li>
 *   <li>Customer web portals for loyalty point exchange</li>
 *   <li>External API endpoints for voucher operations</li>
 *   <li>Third-party loyalty applications</li>
 *   <li>Partner booking systems</li>
 * </ul>
 * 
 * <p><strong>Redemption Process:</strong></p>
 * <ol>
 *   <li>Customer selects a voucher pack for redemption</li>
 *   <li>System validates customer eligibility and loyalty point balance</li>
 *   <li>Voucher pack availability and status are verified</li>
 *   <li>Loyalty points are deducted from customer account</li>
 *   <li>New voucher is created and assigned to the customer</li>
 *   <li>Redemption confirmation is returned to the customer</li>
 * </ol>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Customer must have sufficient loyalty points for redemption</li>
 *   <li>Voucher pack must be in PUBLISHED status</li>
 *   <li>Voucher pack must have available quantity</li>
 *   <li>Voucher pack must be within validity period</li>
 *   <li>Customer must not exceed redemption limits</li>
 *   <li>Redemption is typically limited to one voucher per request</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>Customer authentication is required for all redemption requests</li>
 *   <li>Customer ID must match the authenticated user</li>
 *   <li>Fraud prevention through redemption limits and monitoring</li>
 *   <li>Audit trail for all redemption transactions</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see com.poly.promotion.domain.core.entity.Voucher
 * @see com.poly.promotion.domain.core.entity.VoucherPack
 * @see com.poly.promotion.domain.application.api.external.VoucherExternalApi
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRedeemRequest {
    
    /**
     * Unique identifier of the customer requesting the voucher redemption.
     * 
     * <p>This field identifies the customer who is redeeming the voucher and
     * is used to validate authentication, check loyalty point balance, and
     * assign the redeemed voucher to the correct customer account.</p>
     * 
     * <p><strong>Validation Requirements:</strong></p>
     * <ul>
     *   <li>Must not be null or empty</li>
     *   <li>Must match the authenticated user's customer ID</li>
     *   <li>Must be a valid customer identifier format</li>
     * </ul>
     * 
     * <p><strong>Security Implications:</strong></p>
     * <ul>
     *   <li>Used for customer authentication and authorization</li>
     *   <li>Prevents unauthorized access to other customers' accounts</li>
     *   <li>Required for audit trail and transaction logging</li>
     * </ul>
     */
    String customerId;
    
    /**
     * Unique identifier of the voucher pack from which the voucher will be redeemed.
     * 
     * <p>This field identifies the specific voucher pack that the customer wants
     * to redeem a voucher from. The system will validate the pack's availability,
     * status, and required loyalty points before processing the redemption.</p>
     * 
     * <p><strong>Validation Requirements:</strong></p>
     * <ul>
     *   <li>Must not be null</li>
     *   <li>Must be a positive number</li>
     *   <li>Must reference an existing voucher pack</li>
     *   <li>Voucher pack must be in PUBLISHED status</li>
     * </ul>
     * 
     * <p><strong>Business Logic:</strong></p>
     * <ul>
     *   <li>Determines the discount amount and type for the voucher</li>
     *   <li>Defines the validity period for the redeemed voucher</li>
     *   <li>Specifies the loyalty points required for redemption</li>
     *   <li>Controls the quantity of vouchers available for redemption</li>
     * </ul>
     */
    Long voucherPackId;
}
