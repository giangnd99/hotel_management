package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.promotion.domain.core.exception.PromotionDomainException;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <h2>VoucherPack Entity</h2>
 * 
 * <p>Represents a collection of vouchers that share common characteristics such as discount amount, 
 * validity period, and required loyalty points for redemption. A VoucherPack serves as a template 
 * for creating individual vouchers when customers redeem them using their loyalty points.</p>
 * 
 * <p>This entity is responsible for managing the lifecycle of voucher packs, including:</p>
 * <ul>
 *   <li>Creation and configuration of voucher pack parameters</li>
 *   <li>Stock management (quantity tracking)</li>
 *   <li>Status transitions (PENDING → PUBLISHED → CLOSED/EXPIRED)</li>
 *   <li>Validation of business rules and constraints</li>
 * </ul>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Voucher packs must have a positive quantity</li>
 *   <li>Required points must be positive</li>
 *   <li>Pack validity dates must be logical (validFrom ≤ validTo)</li>
 *   <li>Only published packs can have vouchers redeemed from them</li>
 *   <li>Packs are automatically closed when stock reaches zero</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see Voucher
 * @see Discount
 * @see DateRange
 * @see VoucherPackStatus
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPack extends BaseEntity<VoucherPackId> {
    
    /**
     * Human-readable description of the voucher pack.
     * Used to explain what the vouchers in this pack offer to customers.
     */
    String description;
    
    /**
     * The discount configuration for vouchers in this pack.
     * Can be either a percentage discount or a fixed amount discount.
     */
    Discount discountAmount;
    
    /**
     * The validity period for individual vouchers created from this pack.
     * Defines how long each redeemed voucher remains valid for use.
     */
    DateRange voucherValidRange;
    
    /**
     * Number of loyalty points required to redeem a voucher from this pack.
     * Must be positive and represents the "cost" to customers.
     */
    Long requiredPoints;
    
    /**
     * Current available quantity of vouchers in this pack.
     * Decreases as vouchers are redeemed and reaches zero when pack is exhausted.
     */
    Integer quantity;
    
    /**
     * Start date when this voucher pack becomes available for redemption.
     * Null means the pack is available immediately upon creation.
     */
    LocalDate packValidFrom;
    
    /**
     * End date when this voucher pack expires and becomes unavailable.
     * After this date, the pack status should be set to EXPIRED.
     */
    LocalDate packValidTo;
    
    /**
     * Timestamp when this voucher pack was created.
     * Automatically set during creation and should not be modified.
     */
    LocalDateTime createdAt;
    
    /**
     * Identifier of the user who created this voucher pack.
     * Used for audit trails and accountability.
     */
    String createdBy;
    
    /**
     * Timestamp when this voucher pack was last modified.
     * Automatically updated on each modification.
     */
    LocalDateTime updatedAt;
    
    /**
     * Identifier of the user who last modified this voucher pack.
     * Used for audit trails and accountability.
     */
    String updatedBy;
    
    /**
     * Current status of the voucher pack.
     * Controls the pack's availability and operational state.
     */
    VoucherPackStatus status;

    /**
     * Validates that the pack's validity dates are logically consistent.
     * 
     * <p>This method ensures that if both packValidFrom and packValidTo are set,
     * the from date is not after the to date.</p>
     * 
     * @throws PromotionDomainException if the dates are invalid
     */
    public void validatePackDates() {
        if (packValidFrom != null && packValidTo != null && packValidFrom.isAfter(packValidTo)) {
            throw new PromotionDomainException("Pack valid from date must be before valid to date");
        }
    }

    /**
     * Validates that the pack quantity is positive.
     * 
     * @throws PromotionDomainException if quantity is null or non-positive
     */
    public void validateQuantity() {
        if (quantity == null || quantity <= 0) {
            throw new PromotionDomainException("Quantity must be positive");
        }
    }

    /**
     * Validates that the required points are positive.
     * 
     * @throws PromotionDomainException if required points is null or non-positive
     */
    public void validateRequiredPoints() {
        if (requiredPoints == null || requiredPoints <= 0) {
            throw new PromotionDomainException("Required points must be positive");
        }
    }

    /**
     * Determines if this voucher pack is currently active and available for redemption.
     * 
     * <p>A pack is considered active if it meets all of the following criteria:</p>
     * <ul>
     *   <li>Status is PUBLISHED</li>
     *   <li>Current date is within the pack's validity period (if dates are set)</li>
     *   <li>Quantity is greater than zero</li>
     * </ul>
     * 
     * @return true if the pack is active and available for redemption, false otherwise
     */
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return status == VoucherPackStatus.PUBLISHED &&
               (packValidFrom == null || !today.isBefore(packValidFrom)) &&
               (packValidTo == null || !today.isAfter(packValidTo)) &&
               quantity > 0;
    }

    /**
     * Checks if this pack can provide the requested quantity of vouchers.
     * 
     * <p>This method combines the active status check with quantity availability.</p>
     * 
     * @param requestedQuantity the number of vouchers requested
     * @return true if the pack can provide the requested quantity, false otherwise
     */
    public boolean canRedeem(Integer requestedQuantity) {
        return isActive() && requestedQuantity > 0 && requestedQuantity <= quantity;
    }

    /**
     * Reduces the pack's quantity after vouchers have been redeemed.
     * 
     * <p>This method automatically closes the pack if the quantity reaches zero.
     * It should be called after successfully creating vouchers from this pack.</p>
     * 
     * @param quantity the number of vouchers redeemed
     * @throws PromotionDomainException if the pack cannot provide the requested quantity
     */
    public void redeem(Integer quantity) {
        if (!canRedeem(quantity)) {
            throw new PromotionDomainException("Cannot redeem " + quantity + " vouchers from pack " + getId());
        }
        this.quantity -= quantity;
        if (this.quantity <= 0) {
            this.status = VoucherPackStatus.CLOSED;
        }
    }
}
