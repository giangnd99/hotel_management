package com.poly.promotion.domain.application.dto.response.external;

import com.poly.promotion.domain.core.entity.VoucherPack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <h2>VoucherPackExternalResponse Class</h2>
 * 
 * <p>External response Data Transfer Object (DTO) for voucher pack information in the promotion system.
 * This class provides filtered and sanitized voucher pack data for external consumption by customers,
 * third-party applications, and public-facing systems.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>Customer Information:</strong> Provides voucher pack details for customer decision-making</li>
 *   <li><strong>External Consumption:</strong> Designed for safe exposure to external systems</li>
 *   <li><strong>Data Filtering:</strong> Removes sensitive internal information and business logic</li>
 *   <li><strong>Public Display:</strong> Suitable for customer-facing applications and marketing</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This DTO is typically used by:</p>
 * <ul>
 *   <li>Customer mobile applications for voucher browsing</li>
 *   <li>Customer web portals for loyalty program information</li>
 *   <li>Third-party booking systems and travel platforms</li>
 *   <li>Public promotional displays and marketing materials</li>
 *   <li>Partner hotel applications and integrations</li>
 * </ul>
 * 
 * <p><strong>Data Filtering:</strong></p>
 * <ul>
 *   <li>Only customer-relevant information is included</li>
 *   <li>Internal business logic is excluded</li>
 *   <li>Sensitive administrative data is filtered out</li>
 *   <li>External-friendly formatting is applied</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>No internal system details are exposed</li>
 *   <li>Customer privacy is maintained</li>
 *   <li>Business logic is hidden from external view</li>
 *   <li>Rate limiting and access control may be applied</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPack
 * @see com.poly.promotion.domain.application.api.external.VoucherPackExternalApi
 */
@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPackExternalResponse {
    
    /**
     * Human-readable description of the voucher pack.
     * 
     * <p>This field provides a clear explanation of what the vouchers in this
     * pack offer to customers. It is designed to help customers understand
     * the value proposition and make informed redemption decisions.</p>
     * 
     * <p><strong>Content Examples:</strong></p>
     * <ul>
     *   <li>"20% off on room bookings"</li>
     *   <li>"$50 discount on restaurant orders"</li>
     *   <li>"Free spa treatment voucher"</li>
     *   <li>"Complimentary breakfast voucher"</li>
     * </ul>
     * 
     * <p><strong>Customer Impact:</strong></p>
     * <ul>
     *   <li>Helps customers understand voucher benefits</li>
     *   <li>Influences redemption decisions</li>
     *   <li>Supports marketing and promotional efforts</li>
     *   <li>Provides clear value communication</li>
     * </ul>
     */
    String description;
    
    /**
     * The discount amount for vouchers in this pack.
     * 
     * <p>This field represents the monetary value or percentage of the discount
     * that will be applied when vouchers from this pack are used. The actual
     * discount type (percentage or fixed amount) is determined by the business logic
     * but not exposed to external consumers.</p>
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
     *   <li>Helps customers assess voucher value</li>
     *   <li>Influences loyalty point investment decisions</li>
     *   <li>Supports cost-benefit analysis</li>
     *   <li>Provides transparency in promotional offers</li>
     * </ul>
     */
    BigDecimal discountAmount;
    
    /**
     * The validity period for individual vouchers created from this pack.
     * 
     * <p>This field specifies how long each redeemed voucher will be valid
     * from the time of redemption. It helps customers plan their voucher usage
     * and understand the time commitment required.</p>
     * 
     * <p><strong>Format Examples:</strong></p>
     * <ul>
     *   <li>"30 DAYS" - Vouchers valid for 30 days after redemption</li>
     *   <li>"2 WEEKS" - Vouchers valid for 2 weeks after redemption</li>
     *   <li>"6 MONTHS" - Vouchers valid for 6 months after redemption</li>
     *   <li>"1 YEAR" - Vouchers valid for 1 year after redemption</li>
     * </ul>
     * 
     * <p><strong>Customer Planning:</strong></p>
     * <ul>
     *   <li>Helps customers plan redemption timing</li>
     *   <li>Influences usage planning and scheduling</li>
     *   <li>Supports travel and booking decisions</li>
     *   <li>Provides clear validity expectations</li>
     * </ul>
     */
    String validRange;
    
    /**
     * Number of loyalty points required to redeem a voucher from this pack.
     * 
     * <p>This field specifies the "cost" in loyalty points that customers
     * must pay to redeem a voucher from this pack. It represents the exchange
     * rate between loyalty points and voucher value, helping customers make
     * informed investment decisions.</p>
     * 
     * <p><strong>Business Logic:</strong></p>
     * <ul>
     *   <li>Must be positive and non-zero</li>
     *   <li>Represents the loyalty point investment required</li>
     *   <li>Used for customer eligibility validation</li>
     *   <li>Affects customer redemption decisions</li>
     * </ul>
     * 
     * <p><strong>Customer Decision Making:</strong></p>
     * <ul>
     *   <li>Helps customers assess affordability</li>
     *   <li>Influences loyalty point saving strategies</li>
     *   <li>Supports cost-benefit analysis</li>
     *   <li>Provides clear redemption requirements</li>
     * </ul>
     */
    Long requiredPoints;
    
    /**
     * Available quantity of vouchers in this pack.
     * 
     * <p>This field indicates how many vouchers are currently available
     * for customer redemption from this pack. It helps customers understand
     * availability and urgency for redemption decisions.</p>
     * 
     * <p><strong>Quantity Management:</strong></p>
     * <ul>
     *   <li>Decreases with each successful redemption</li>
     *   <li>Cannot go below zero</li>
     *   <li>May trigger automatic pack closure when reaching zero</li>
     *   <li>Can be replenished by administrators</li>
     * </ul>
     * 
     * <p><strong>Customer Impact:</strong></p>
     * <ul>
     *   <li>Creates urgency for limited availability</li>
     *   <li>Influences redemption timing decisions</li>
     *   <li>Supports scarcity marketing strategies</li>
     *   <li>Provides transparency in availability</li>
     * </ul>
     */
    Integer quantity;
    
    /**
     * Start date when this voucher pack becomes available for redemption.
     * 
     * <p>This field defines the beginning of the validity period for the
     * voucher pack itself. Before this date, customers cannot redeem
     * vouchers from this pack, helping them plan their redemption timing.</p>
     * 
     * <p><strong>Validity Logic:</strong></p>
     * <ul>
     *   <li>Null value means immediate availability</li>
     *   <li>Must be before or equal to validTo if both are set</li>
     *   <li>Used for automatic status transitions</li>
     *   <li>Affects customer visibility and redemption eligibility</li>
     * </ul>
     * 
     * <p><strong>Customer Planning:</strong></p>
     * <ul>
     *   <li>Helps customers plan redemption timing</li>
     *   <li>Influences loyalty point accumulation strategies</li>
     *   <li>Supports advance planning for future redemptions</li>
     *   <li>Provides clear availability expectations</li>
     * </ul>
     */
    LocalDate validFrom;
    
    /**
     * End date when this voucher pack expires and becomes unavailable.
     * 
     * <p>This field defines the end of the validity period for the voucher
     * pack. After this date, the pack becomes unavailable for redemption,
     * creating urgency for customers to act before expiration.</p>
     * 
     * <p><strong>Expiration Logic:</strong></p>
     * <ul>
     *   <li>Must be after or equal to validFrom if both are set</li>
     *   <li>Triggers automatic status change to EXPIRED</li>
     *   <li>Prevents new redemptions after expiration</li>
     *   <li>Used for automatic cleanup and reporting</li>
     * </ul>
     * 
     * <p><strong>Customer Urgency:</strong></p>
     * <ul>
     *   <li>Creates urgency for redemption decisions</li>
     *   <li>Influences loyalty point spending priorities</li>
     *   <li>Supports time-limited promotional strategies</li>
     *   <li>Provides clear expiration expectations</li>
     * </ul>
     */
    LocalDate validTo;
    
    /**
     * Current status of the voucher pack in the system.
     * 
     * <p>This field indicates the current lifecycle state of the voucher
     * pack. The status determines whether customers can redeem vouchers
     * from it and provides transparency about pack availability.</p>
     * 
     * <p><strong>Status Values:</strong></p>
     * <ul>
     *   <li>PENDING - Created but not yet available for redemption</li>
     *   <li>PUBLISHED - Available for customer redemption</li>
     *   <li>CLOSED - Manually closed by administrators</li>
     *   <li>EXPIRED - Automatically expired due to validity period</li>
     * </ul>
     * 
     * <p><strong>Customer Impact:</strong></p>
     * <ul>
     *   <li>Determines redemption eligibility</li>
     *   <li>Provides transparency about pack state</li>
     *   <li>Influences customer expectations</li>
     *   <li>Supports clear communication about availability</li>
     * </ul>
     */
    String packStatus;

    /**
     * Creates a VoucherPackExternalResponse from a VoucherPack domain entity.
     * 
     * <p>This static factory method converts a domain entity to an external
     * response DTO, ensuring that only appropriate information is exposed
     * to external consumers while maintaining data privacy and security.</p>
     * 
     * <p><strong>Conversion Process:</strong></p>
     * <ol>
     *   <li>Extracts customer-relevant voucher pack information</li>
     *   <li>Converts domain value objects to appropriate formats</li>
     *   <li>Filters out sensitive internal information</li>
     *   <li>Applies external-friendly formatting</li>
     * </ol>
     * 
     * <p><strong>Data Mapping:</strong></p>
     * <ul>
     *   <li>Description: Directly mapped for customer understanding</li>
     *   <li>Discount: Converted from Discount interface to BigDecimal</li>
     *   <li>Date Range: Converted from DateRange to string representation</li>
     *   <li>Status: Converted from enum to string representation</li>
     *   <li>Timestamps: Excluded for external consumption</li>
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
     * @param voucherPack the VoucherPack domain entity to convert
     * @return a VoucherPackExternalResponse with filtered voucher pack data
     * @throws IllegalArgumentException if voucherPack is null
     */
    public static VoucherPackExternalResponse fromEntity(VoucherPack voucherPack) {
        if (voucherPack == null) {
            throw new IllegalArgumentException("VoucherPack cannot be null");
        }
        
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
