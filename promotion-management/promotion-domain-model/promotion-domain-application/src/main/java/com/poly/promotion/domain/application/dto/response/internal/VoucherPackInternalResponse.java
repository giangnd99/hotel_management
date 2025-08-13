package com.poly.promotion.domain.application.dto.response.internal;

import com.poly.promotion.domain.core.entity.VoucherPack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <h2>VoucherPackInternalResponse Class</h2>
 * 
 * <p>Internal response Data Transfer Object (DTO) for voucher pack information in the promotion system.
 * This class provides comprehensive voucher pack data for internal administrative and system operations,
 * including all business logic details and administrative metadata.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>Administrative Data:</strong> Provides complete voucher pack information for internal management</li>
 *   <li><strong>Business Logic Visibility:</strong> Exposes internal business rules and calculations</li>
 *   <li><strong>Audit Trail:</strong> Includes creation and modification timestamps for compliance</li>
 *   <li><strong>System Operations:</strong> Supports internal system maintenance and monitoring</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This DTO is typically used by:</p>
 * <ul>
 *   <li>Administrative web interfaces for voucher pack management</li>
 *   <li>Internal system services for business operations</li>
 *   <li>System maintenance and monitoring tools</li>
 *   <li>Internal reporting and analytics systems</li>
 *   <li>Audit and compliance reporting</li>
 * </ul>
 * 
 * <p><strong>Data Completeness:</strong></p>
 * <ul>
 *   <li>Full voucher pack configuration details</li>
 *   <li>Complete administrative metadata</li>
 *   <li>Internal business logic information</li>
 *   <li>Audit trail and change history</li>
 * </ul>
 * 
 * <p><strong>Security Considerations:</strong></p>
 * <ul>
 *   <li>Internal use only - not exposed to external systems</li>
 *   <li>Access restricted to authenticated internal users</li>
 *   <li>Role-based access control for sensitive operations</li>
 *   <li>Audit logging for all data access</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPack
 * @see com.poly.promotion.domain.application.api.internal.VoucherPackInternalApi
 */
@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPackInternalResponse {
    
    /**
     * Unique identifier of the voucher pack.
     * 
     * <p>This field provides the primary key for identifying the voucher pack
     * in the system. It is used for all administrative operations, updates,
     * and references to this specific voucher pack.</p>
     * 
     * <p><strong>Usage:</strong></p>
     * <ul>
     *   <li>Primary key for database operations</li>
     *   <li>Reference identifier for updates and modifications</li>
     *   <li>Audit trail correlation</li>
     *   <li>System integration and API calls</li>
     * </ul>
     */
    Long id;
    
    /**
     * Human-readable description of the voucher pack.
     * 
     * <p>This field provides a clear explanation of what the vouchers in this
     * pack offer to customers. It is used for administrative identification
     * and customer communication.</p>
     * 
     * <p><strong>Content Examples:</strong></p>
     * <ul>
     *   <li>"20% off on room bookings"</li>
     *   <li>"$50 discount on restaurant orders"</li>
     *   <li>"Free spa treatment voucher"</li>
     *   <li>"Complimentary breakfast voucher"</li>
     * </ul>
     */
    String description;
    
    /**
     * The discount amount for vouchers in this pack.
     * 
     * <p>This field represents the monetary value or percentage of the discount
     * that will be applied when vouchers from this pack are used. The actual
     * discount type (percentage or fixed amount) is determined by the business logic.</p>
     * 
     * <p><strong>Value Interpretation:</strong></p>
     * <ul>
     *   <li>Values ≤ 100: Represent percentage discounts (e.g., 15.0 = 15%)</li>
     *   <li>Values > 100: Represent fixed amount discounts in the system's base currency</li>
     *   <li>Precision: Uses BigDecimal for accurate financial calculations</li>
     * </ul>
     */
    BigDecimal discountAmount;
    
    /**
     * The validity period for individual vouchers created from this pack.
     * 
     * <p>This field specifies how long each redeemed voucher will be valid
     * from the time of redemption. The format is a human-readable string
     * representation of the DateRange value object.</p>
     * 
     * <p><strong>Format Examples:</strong></p>
     * <ul>
     *   <li>"30 DAYS" - Vouchers valid for 30 days after redemption</li>
     *   <li>"2 WEEKS" - Vouchers valid for 2 weeks after redemption</li>
     *   <li>"6 MONTHS" - Vouchers valid for 6 months after redemption</li>
     *   <li>"1 YEAR" - Vouchers valid for 1 year after redemption</li>
     * </ul>
     */
    String validRange;
    
    /**
     * Number of loyalty points required to redeem a voucher from this pack.
     * 
     * <p>This field specifies the "cost" in loyalty points that customers
     * must pay to redeem a voucher from this pack. It represents the exchange
     * rate between loyalty points and voucher value.</p>
     * 
     * <p><strong>Business Logic:</strong></p>
     * <ul>
     *   <li>Must be positive and non-zero</li>
     *   <li>Represents the loyalty point investment required</li>
     *   <li>Used for customer eligibility validation</li>
     *   <li>Affects customer redemption decisions</li>
     * </ul>
     */
    Long requiredPoints;
    
    /**
     * Available quantity of vouchers in this pack.
     * 
     * <p>This field indicates how many vouchers are currently available
     * for customer redemption from this pack. It decreases as customers
     * redeem vouchers and may be replenished through administrative actions.</p>
     * 
     * <p><strong>Quantity Management:</strong></p>
     * <ul>
     *   <li>Decreases with each successful redemption</li>
     *   <li>Cannot go below zero</li>
     *   <li>May trigger automatic pack closure when reaching zero</li>
     *   <li>Can be replenished by administrators</li>
     * </ul>
     */
    Integer quantity;
    
    /**
     * Start date when this voucher pack becomes available for redemption.
     * 
     * <p>This field defines the beginning of the validity period for the
     * voucher pack itself. Before this date, customers cannot redeem
     * vouchers from this pack.</p>
     * 
     * <p><strong>Validity Logic:</strong></p>
     * <ul>
     *   <li>Null value means immediate availability</li>
     *   <li>Must be before or equal to validTo if both are set</li>
     *   <li>Used for automatic status transitions</li>
     *   <li>Affects customer visibility and redemption eligibility</li>
     * </ul>
     */
    LocalDate validFrom;
    
    /**
     * End date when this voucher pack expires and becomes unavailable.
     * 
     * <p>This field defines the end of the validity period for the voucher
     * pack. After this date, the pack status should automatically transition
     * to EXPIRED and become unavailable for redemption.</p>
     * 
     * <p><strong>Expiration Logic:</strong></p>
     * <ul>
     *   <li>Must be after or equal to validFrom if both are set</li>
     *   <li>Triggers automatic status change to EXPIRED</li>
     *   <li>Prevents new redemptions after expiration</li>
     *   <li>Used for automatic cleanup and reporting</li>
     * </ul>
     */
    LocalDate validTo;
    
    /**
     * Timestamp when this voucher pack was created in the system.
     * 
     * <p>This field records the exact date and time when the voucher pack
     * was first created. It is used for audit trails, compliance reporting,
     * and system monitoring purposes.</p>
     * 
     * <p><strong>Audit Usage:</strong></p>
     * <ul>
     *   <li>Creation date tracking for compliance</li>
     *   <li>System performance monitoring</li>
     *   <li>Administrative workflow tracking</li>
     *   <li>Historical data analysis</li>
     * </ul>
     */
    LocalDateTime createdAt;
    
    /**
     * Identifier of the user who created this voucher pack.
     * 
     * <p>This field records which administrator or system user created
     * the voucher pack. It provides accountability and supports audit
     * trail requirements.</p>
     * 
     * <p><strong>Accountability Features:</strong></p>
     * <ul>
     *   <li>User identification for audit trails</li>
     *   <li>Responsibility assignment for changes</li>
     *   <li>Compliance with regulatory requirements</li>
     *   <li>Support for change management processes</li>
     * </ul>
     */
    String createdBy;
    
    /**
     * Timestamp when this voucher pack was last modified.
     * 
     * <p>This field records the most recent date and time when any
     * aspect of the voucher pack was modified. It helps track the
     * freshness of the data and supports change management processes.</p>
     * 
     * <p><strong>Change Tracking:</strong></p>
     * <ul>
     *   <li>Last modification timestamp</li>
     *   <li>Data freshness indicators</li>
     *   <li>Change management support</li>
     *   <li>System synchronization validation</li>
     * </ul>
     */
    LocalDateTime updatedAt;
    
    /**
     * Identifier of the user who last modified this voucher pack.
     * 
     * <p>This field records which administrator or system user made
     * the most recent modification to the voucher pack. It provides
     * accountability for changes and supports audit requirements.</p>
     * 
     * <p><strong>Modification Tracking:</strong></p>
     * <ul>
     *   <li>Change accountability tracking</li>
     *   <li>Audit trail maintenance</li>
     *   <li>User responsibility assignment</li>
     *   <li>Compliance with change management policies</li>
     * </ul>
     */
    String updatedBy;
    
    /**
     * Current status of the voucher pack in the system.
     * 
     * <p>This field indicates the current lifecycle state of the voucher
     * pack. The status determines what operations can be performed on
     * the pack and whether customers can redeem vouchers from it.</p>
     * 
     * <p><strong>Status Values:</strong></p>
     * <ul>
     *   <li>PENDING - Created but not yet available for redemption</li>
     *   <li>PUBLISHED - Available for customer redemption</li>
     *   <li>CLOSED - Manually closed by administrators</li>
     *   <li>EXPIRED - Automatically expired due to validity period</li>
     * </ul>
     */
    String status;

    /**
     * Creates a VoucherPackInternalResponse from a VoucherPack domain entity.
     * 
     * <p>This static factory method converts a domain entity to an internal
     * response DTO, ensuring that all necessary information is preserved
     * for administrative operations and internal system use.</p>
     * 
     * <p><strong>Conversion Process:</strong></p>
     * <ol>
     *   <li>Extracts basic voucher pack information</li>
     *   <li>Converts domain value objects to appropriate formats</li>
     *   <li>Includes administrative metadata and timestamps</li>
     *   <li>Preserves business logic information</li>
     * </ol>
     * 
     * <p><strong>Data Mapping:</strong></p>
     * <ul>
     *   <li>ID: Extracted from VoucherPackId value object</li>
     *   <li>Discount: Converted from Discount interface to BigDecimal</li>
     *   <li>Date Range: Converted from DateRange to string representation</li>
     *   <li>Status: Converted from enum to string representation</li>
     *   <li>Timestamps: Directly mapped from entity fields</li>
     * </ul>
     * 
     * @param voucherPack the VoucherPack domain entity to convert
     * @return a VoucherPackInternalResponse with all voucher pack data
     * @throws IllegalArgumentException if voucherPack is null
     */
    public static VoucherPackInternalResponse fromEntity(VoucherPack voucherPack) {
        if (voucherPack == null) {
            throw new IllegalArgumentException("VoucherPack cannot be null");
        }
        
        return VoucherPackInternalResponse.builder()
                .id(voucherPack.getId().getValue())
                .description(voucherPack.getDescription())
                .discountAmount(voucherPack.getDiscountAmount().getValue())
                .validRange(voucherPack.getVoucherValidRange().toString())
                .requiredPoints(voucherPack.getRequiredPoints())
                .quantity(voucherPack.getQuantity())
                .validFrom(voucherPack.getPackValidFrom())
                .validTo(voucherPack.getPackValidTo())
                .createdAt(voucherPack.getCreatedAt())
                .createdBy(voucherPack.getCreatedBy())
                .updatedAt(voucherPack.getUpdatedAt())
                .updatedBy(voucherPack.getUpdatedBy())
                .status(voucherPack.getStatus().name())
                .build();
    }
}
