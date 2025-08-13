package com.poly.promotion.domain.application.dto.response.external;

import com.poly.promotion.domain.core.entity.Voucher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <h2>VoucherExternalResponse Class</h2>
 * 
 * <p>External response Data Transfer Object (DTO) for individual voucher information in the promotion system.
 * This class provides filtered and sanitized voucher data for external consumption by customers,
 * allowing them to view and manage their redeemed vouchers through external applications.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>Customer Voucher Management:</strong> Provides voucher details for customer viewing and usage</li>
 *   <li><strong>External Consumption:</strong> Designed for safe exposure to external customer applications</li>
 *   <li><strong>Data Filtering:</strong> Removes sensitive internal information and business logic</li>
 *   <li><strong>Customer Privacy:</strong> Ensures only customer-specific information is exposed</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This DTO is typically used by:</p>
 * <ul>
 *   <li>Customer mobile applications for voucher management</li>
 *   <li>Customer web portals for loyalty program access</li>
 *   <li>Third-party loyalty applications and integrations</li>
 *   <li>Partner booking systems for voucher validation</li>
 *   <li>Customer service applications for voucher support</li>
 * </ul>
 * 
 * <p><strong>Data Filtering:</strong></p>
 * <ul>
 *   <li>Only customer's own voucher information is included</li>
 *   <li>Internal business logic is excluded</li>
 *   <li>Sensitive administrative data is filtered out</li>
 *   <li>External-friendly formatting is applied</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>Customer authentication is required for access</li>
 *   <li>Only customer's own vouchers are accessible</li>
 *   <li>No internal system details are exposed</li>
 *   <li>Customer privacy is maintained</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see Voucher
 * @see com.poly.promotion.domain.application.api.external.VoucherExternalApi
 */
@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherExternalResponse {
    
    /**
     * Unique voucher code that customers use to redeem their discount.
     * 
     * <p>This field provides the primary identifier that customers use to
     * apply their voucher during transactions. The voucher code is unique
     * across the entire system and serves as the customer's proof of
     * voucher ownership.</p>
     * 
     * <p><strong>Usage Examples:</strong></p>
     * <ul>
     *   <li>Entering during online booking processes</li>
     *   <li>Presenting to staff during in-person transactions</li>
     *   <li>Using in mobile applications for automatic application</li>
     *   <li>Referencing during customer service interactions</li>
     * </ul>
     * 
     * <p><strong>Security Features:</strong></p>
     * <ul>
     *   <li>Unique across the entire system</li>
     *   <li>Cannot be easily guessed or generated</li>
     *   <li>Linked to specific customer ownership</li>
     *   <li>Validated during each usage attempt</li>
     * </ul>
     */
    String voucherCode;
    
    /**
     * The discount amount that will be applied when this voucher is used.
     * 
     * <p>This field represents the monetary value or percentage of the discount
     * that customers will receive when they use this voucher. The actual
     * discount type (percentage or fixed amount) is determined by the business
     * logic but not exposed to external consumers.</p>
     * 
     * <p><strong>Value Interpretation:</strong></p>
     * <ul>
     *   <li>Values ≤ 100: Represent percentage discounts (e.g., 15.0 = 15%)</li>
     *   <li>Values > 100: Represent fixed amount discounts in the system's base currency</li>
     *   <li>Precision: Uses BigDecimal for accurate financial calculations</li>
     *   <li>External consumers need to interpret based on business context</li>
     * </ul>
     * 
     * <p><strong>Customer Usage:</strong></p>
     * <ul>
     *   <li>Helps customers understand voucher value</li>
     *   <li>Influences usage timing and transaction planning</li>
     *   <li>Supports cost-benefit analysis for purchases</li>
     *   <li>Provides transparency in discount application</li>
     * </ul>
     */
    BigDecimal discountAmount;
    
    /**
     * Timestamp when this voucher was redeemed by the customer.
     * 
     * <p>This field records the exact date and time when the customer
     * successfully redeemed this voucher from a voucher pack using their
     * loyalty points. It marks the beginning of the voucher's validity period.</p>
     * 
     * <p><strong>Business Logic:</strong></p>
     * <ul>
     *   <li>Represents the start of voucher validity period</li>
     *   <li>Used for calculating voucher expiration dates</li>
     *   <li>Marks the completion of loyalty point redemption</li>
     *   <li>Provides audit trail for redemption transactions</li>
     * </ul>
     * 
     * <p><strong>Customer Impact:</strong></p>
     * <ul>
     *   <li>Helps customers track when they redeemed the voucher</li>
     *   <li>Influences usage planning and urgency</li>
     *   <li>Supports customer service and support requests</li>
     *   <li>Provides clear redemption history</li>
     * </ul>
     */
    LocalDateTime redeemedAt;
    
    /**
     * Expiration date and time when this voucher becomes invalid.
     * 
     * <p>This field specifies the exact moment when the voucher expires
     * and can no longer be used. After this timestamp, the voucher
     * status should automatically transition to EXPIRED.</p>
     * 
     * <p><strong>Validity Logic:</strong></p>
     * <ul>
     *   <li>Calculated based on redemption date and voucher pack validity period</li>
     *   <li>Cannot be extended or modified after redemption</li>
     *   <li>Triggers automatic status change to EXPIRED</li>
     *   <li>Prevents usage after expiration</li>
     * </ul>
     * 
     * <p><strong>Customer Planning:</strong></p>
     * <ul>
     *   <li>Creates urgency for voucher usage</li>
     *   <li>Influences transaction timing decisions</li>
     *   <li>Supports usage planning and scheduling</li>
     *   <li>Provides clear expiration expectations</li>
     * </ul>
     */
    LocalDateTime validTo;
    
    /**
     * Current status of the voucher in the system.
     * 
     * <p>This field indicates the current lifecycle state of the voucher,
     * determining whether it can be used, has already been used, or has
     * expired. The status provides transparency about voucher availability.</p>
     * 
     * <p><strong>Status Values:</strong></p>
     * <ul>
     *   <li>PENDING - Voucher is created but not yet available for use</li>
     *   <li>REDEEMED - Voucher has been redeemed and is available for use</li>
     *   <li>USED - Voucher has been applied to a transaction</li>
     *   <li>EXPIRED - Voucher has expired and cannot be used</li>
     * </ul>
     * 
     * <p><strong>Customer Impact:</strong></p>
     * <ul>
     *   <li>Determines voucher usability</li>
     *   <li>Provides transparency about voucher state</li>
     *   <li>Influences usage decisions and planning</li>
     *   <li>Supports clear communication about availability</li>
     * </ul>
     */
    String voucherStatus;

    /**
     * Creates a VoucherExternalResponse from a Voucher domain entity.
     * 
     * <p>This static factory method converts a domain entity to an external
     * response DTO, ensuring that only appropriate information is exposed
     * to external consumers while maintaining data privacy and security.</p>
     * 
     * <p><strong>Conversion Process:</strong></p>
     * <ol>
     *   <li>Extracts customer-relevant voucher information</li>
     *   <li>Converts domain value objects to appropriate formats</li>
     *   <li>Filters out sensitive internal information</li>
     *   <li>Applies external-friendly formatting</li>
     * </ol>
     * 
     * <p><strong>Data Mapping:</strong></p>
     * <ul>
     *   <li>Voucher Code: Directly mapped for customer usage</li>
     *   <li>Discount: Converted from Discount interface to BigDecimal</li>
     *   <li>Timestamps: Directly mapped for customer planning</li>
     *   <li>Status: Converted from enum to string representation</li>
     *   <li>Internal Logic: Excluded for external consumption</li>
     * </ul>
     * 
     * <p><strong>Security Features:</strong></p>
     * <ul>
     *   <li>Internal business logic is hidden</li>
     *   <li>Administrative metadata is excluded</li>
     *   <li>Customer privacy is maintained</li>
     *   <li>External-friendly data structure</li>
     * </ul>
     * 
     * @param voucher the Voucher domain entity to convert
     * @return a VoucherExternalResponse with filtered voucher data
     * @throws IllegalArgumentException if voucher is null
     */
    public static VoucherExternalResponse fromVoucher(Voucher voucher) {
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher cannot be null");
        }
        
        return VoucherExternalResponse.builder()
                .voucherCode(voucher.getVoucherCode())
                .discountAmount(voucher.getDiscount().getValue())
                .redeemedAt(voucher.getRedeemedAt())
                .validTo(voucher.getValidTo())
                .voucherStatus(voucher.getVoucherStatus().name())
                .build();
    }
}
