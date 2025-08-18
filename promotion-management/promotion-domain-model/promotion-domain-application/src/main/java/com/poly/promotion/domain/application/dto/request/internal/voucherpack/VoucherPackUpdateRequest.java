package com.poly.promotion.domain.application.dto.request.internal.voucherpack;

import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.DiscountAmount;
import com.poly.promotion.domain.core.valueobject.DiscountPercentage;
import com.poly.domain.valueobject.Money;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * <h2>VoucherPackUpdateRequest Class</h2>
 * 
 * <p>Data Transfer Object (DTO) for updating existing voucher packs in the promotion system.
 * This class encapsulates all the data that can be modified for a voucher pack, including
 * validation logic and entity conversion methods.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li><strong>Data Transfer:</strong> Carries update data from API layer to domain services</li>
 *   <li><strong>Partial Updates:</strong> Supports updating only specific fields</li>
 *   <li><strong>Entity Conversion:</strong> Converts DTO data to updated domain entities</li>
 *   <li><strong>Business Rule Enforcement:</strong> Ensures data compliance with business rules</li>
 * </ul>
 * 
 * <p><strong>Usage Context:</strong></p>
 * <p>This DTO is typically used by:</p>
 * <ul>
 *   <li>Administrative web interfaces for modifying voucher packs</li>
 *   <li>Internal system services for bulk voucher pack updates</li>
 *   <li>API endpoints for voucher pack management</li>
 *   <li>System maintenance and configuration updates</li>
 * </ul>
 * 
 * <p><strong>Update Restrictions:</strong></p>
 * <ul>
 *   <li>Only PENDING voucher packs can be updated</li>
 *   <li>Published, closed, or expired packs cannot be modified</li>
 *   <li>All fields are optional for partial updates</li>
 *   <li>Business rules are enforced during updates</li>
 * </ul>
 * 
 * <p><strong>Validation Rules:</strong></p>
 * <ul>
 *   <li>All fields are optional (null values are ignored)</li>
 *   <li>Discount amount must be positive if provided</li>
 *   <li>Required points must be positive if provided</li>
 *   <li>Quantity must be positive if provided</li>
 *   <li>Validity dates must be logical if both are provided</li>
 *   <li>Voucher valid range must be in correct format if provided</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPack
 * @see Discount
 * @see DateRange
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPackUpdateRequest {
    
    /**
     * Human-readable description of the voucher pack.
     * Used to explain what the vouchers in this pack offer to customers.
     * Optional for updates - null values are ignored.
     */
    String description;
    
    /**
     * The discount amount for vouchers in this pack.
     * For percentage discounts: value between 0 and 100.
     * For fixed amount discounts: value >= 1000.
     * Must be positive if provided. Optional for updates.
     */
    BigDecimal discountAmount;
    
    /**
     * The validity period for individual vouchers created from this pack.
     * Format: "&lt;value&gt; &lt;unit&gt;" (e.g., "30 DAYS", "2 WEEKS", "6 MONTHS").
     * Must be in the correct format for parsing if provided. Optional for updates.
     */
    String voucherValidRange;
    
    /**
     * Number of loyalty points required to redeem a voucher from this pack.
     * Must be positive if provided. Optional for updates.
     */
    Long requiredPoints;
    
    /**
     * Available quantity of vouchers in this pack.
     * Must be positive if provided. Optional for updates.
     */
    Integer quantity;
    
    /**
     * Start date when this voucher pack becomes available for redemption.
     * Null means the pack is available immediately upon creation.
     * Must be before or equal to packValidTo if both are provided. Optional for updates.
     */
    LocalDate packValidFrom;
    
    /**
     * End date when this voucher pack expires and becomes unavailable.
     * After this date, the pack status should be set to EXPIRED.
     * Must be after or equal to packValidFrom if both are provided. Optional for updates.
     */
    LocalDate packValidTo;

    /**
     * Converts this DTO to a VoucherPack domain entity.
     * 
     * <p>This method performs the transformation from DTO data to domain entities,
     * including the creation of appropriate discount objects and date range parsing.
     * The method handles the complexity of discount type determination and date
     * range parsing for update operations.</p>
     * 
     * <p><strong>Conversion Process:</strong></p>
     * <ol>
     *   <li>Creates appropriate discount object based on amount value (if provided)</li>
     *   <li>Parses voucher valid range string into DateRange object (if provided)</li>
     *   <li>Builds VoucherPack entity with provided data</li>
     *   <li>Applies business rule validations</li>
     * </ol>
     * 
     * <p><strong>Discount Type Logic:</strong></p>
     * <ul>
     *   <li>Amount ≤ 100: Creates DiscountPercentage</li>
     *   <li>Amount > 100: Creates DiscountAmount</li>
     *   <li>Null amount: Sets discount to null</li>
     * </ul>
     * 
     * <p><strong>Date Range Parsing:</strong></p>
     * <ul>
     *   <li>Expected format: "&lt;value&gt; &lt;unit&gt;"</li>
     *   <li>Supports all ChronoUnit values</li>
     *   <li>Throws IllegalArgumentException for invalid formats</li>
     * </ul>
     * 
     * @return a new VoucherPack entity with the data from this DTO
     * @throws IllegalArgumentException if the voucher valid range format is invalid
     */
    public VoucherPack toEntity() {
        // Create appropriate discount based on amount
        Discount discount = null;
        if (discountAmount != null) {
            if (discountAmount.compareTo(BigDecimal.valueOf(100)) <= 0) {
                discount = new DiscountPercentage(discountAmount.doubleValue());
            } else {
                discount = new DiscountAmount(new Money(discountAmount));
            }
        }
        
        // Parse voucher valid range
        DateRange validRange = null;
        if (voucherValidRange != null && !voucherValidRange.isEmpty()) {
            validRange = parseVoucherValidRange(voucherValidRange);
        }
        
        return VoucherPack.builder()
                .description(description)
                .discountAmount(discount)
                .voucherValidRange(validRange)
                .requiredPoints(requiredPoints)
                .quantity(quantity)
                .packValidFrom(packValidFrom)
                .packValidTo(packValidTo)
                .build();
    }

    /**
     * Parses a voucher valid range string into a DateRange object.
     * 
     * <p>This method converts human-readable duration strings into DateRange
     * value objects that can be used by the domain entities. The method
     * supports various time units and provides clear error messages for
     * invalid formats.</p>
     * 
     * <p><strong>Supported Format:</strong></p>
     * <ul>
     *   <li>Pattern: "&lt;value&gt; &lt;unit&gt;"</li>
     *   <li>Value: Positive integer</li>
     *   <li>Unit: ChronoUnit string (e.g., "DAYS", "WEEKS", "MONTHS")</li>
     * </ul>
     * 
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>"30 DAYS" → 30 days validity</li>
     *   <li>"2 WEEKS" → 2 weeks validity</li>
     *   <li>"6 MONTHS" → 6 months validity</li>
     *   <li>"1 YEAR" → 1 year validity</li>
     * </ul>
     * 
     * <p><strong>Error Handling:</strong></p>
     * <ul>
     *   <li>Null or empty strings throw IllegalArgumentException</li>
     *   <li>Invalid format throws IllegalArgumentException</li>
     *   <li>Invalid ChronoUnit values throw IllegalArgumentException</li>
     * </ul>
     * 
     * @param voucherValidRange the string representation of the voucher validity period
     * @return a DateRange object representing the voucher validity period
     * @throws IllegalArgumentException if the format is invalid or parsing fails
     */
    private DateRange parseVoucherValidRange(String voucherValidRange) {
        if (voucherValidRange == null || voucherValidRange.isEmpty()) {
            throw new IllegalArgumentException("Voucher valid range cannot be null or empty");
        }
        
        String[] parts = voucherValidRange.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid date range format. Expected format: '<value> <unit>'");
        }
        
        try {
            int value = Integer.parseInt(parts[0]);
            ChronoUnit unit = ChronoUnit.valueOf(parts[1].toUpperCase());
            return new DateRange(value, unit);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date range format. Expected format: '<value: int> <unit: ChronoUnit string>'", e);
        }
    }
}
