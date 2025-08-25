package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.CustomerId;
import com.poly.promotion.domain.core.valueobject.DateRange;
import com.poly.promotion.domain.core.valueobject.Discount;
import com.poly.promotion.domain.core.valueobject.VoucherId;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;
import com.poly.promotion.domain.core.exception.VoucherDomainException;

/**
 * <h2>Voucher Entity</h2>
 * 
 * <p>Represents an individual voucher that a customer has redeemed from a voucher pack using their loyalty points.
 * Each voucher is a unique instance with its own validity period and usage status.</p>
 * 
 * <p>This entity manages the lifecycle of individual vouchers, including:</p>
 * <ul>
 *   <li>Creation during redemption from a voucher pack</li>
 *   <li>Validation of voucher eligibility for use</li>
 *   <li>Status transitions (PENDING → REDEEMED → USED/EXPIRED)</li>
 *   <li>Automatic expiration based on validity dates</li>
 * </ul>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Vouchers are created with REDEEMED status during redemption</li>
 *   <li>Vouchers can only be used if they are in REDEEMED status and within validity period</li>
 *   <li>Vouchers automatically expire when they pass their validTo date</li>
 *   <li>Each voucher has a unique voucher code for identification</li>
 * </ul>
 * 
 * <p><strong>Lifecycle:</strong></p>
 * <ol>
 *   <li><strong>REDEEMED:</strong> Created when customer redeems from pack</li>
 *   <li><strong>USED:</strong> Voucher has been applied to a transaction</li>
 *   <li><strong>EXPIRED:</strong> Voucher has passed its validity date</li>
 * </ol>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPack
 * @see Discount
 * @see DateRange
 * @see VoucherStatus
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher extends BaseEntity<VoucherId> {
    
    /**
     * Reference to the customer who owns this voucher.
     * Links the voucher to a specific customer for ownership validation.
     */
    CustomerId customerId;
    
    /**
     * Reference to the voucher pack this voucher was created from.
     * Maintains the relationship between individual vouchers and their source pack.
     */
    VoucherPackId voucherPackId;
    
    /**
     * Unique identifier code for this voucher.
     * Used by customers to apply the voucher during transactions.
     * Generated automatically during voucher creation.
     */
    String voucherCode;
    
    /**
     * The discount configuration for this voucher.
     * Inherited from the voucher pack and determines the discount amount/percentage.
     */
    Discount discount;
    
    /**
     * Timestamp when this voucher was redeemed by the customer.
     * Represents the moment the customer exchanged loyalty points for this voucher.
     */
    LocalDateTime redeemedAt;
    
    /**
     * Expiration timestamp for this voucher.
     * After this time, the voucher becomes invalid and cannot be used.
     * Calculated based on the voucher pack's validity range.
     */
    LocalDateTime validTo;
    
    /**
     * Current status of this voucher.
     * Controls the voucher's availability and operational state.
     */
    VoucherStatus voucherStatus;

    /**
     * Creates a new voucher instance when a customer redeems from a voucher pack.
     * 
     * <p>This factory method initializes a voucher with the following characteristics:</p>
     * <ul>
     *   <li>Status set to REDEEMED</li>
     *   <li>Redeemed timestamp set to current time</li>
     *   <li>Validity period calculated from the voucher pack's range</li>
     *   <li>Discount configuration inherited from the pack</li>
     * </ul>
     * 
     * @param customerId the customer's unique identifier
     * @param voucherPackId the voucher pack's unique identifier
     * @param discount the discount configuration from the voucher pack
     * @param voucherValidRange the validity period configuration from the voucher pack
     * @return a new Voucher instance ready for redemption
     * @throws IllegalArgumentException if customerId is not a valid UUID format
     */
    public static Voucher initRedeem(String customerId, Long voucherPackId, Discount discount, DateRange voucherValidRange) {
        return Voucher.builder()
                .customerId(new CustomerId(UUID.fromString(customerId)))
                .voucherPackId(new VoucherPackId(voucherPackId))
                .voucherCode(UUID.randomUUID().toString()) // Should use a more proper voucher code generation strategy
                .discount(discount)
                .redeemedAt(LocalDateTime.now())
                .validTo(LocalDateTime.now().plus(voucherValidRange.getValue(), voucherValidRange.getUnit()))
                .voucherStatus(VoucherStatus.REDEEMED)
                .build();
    }

    /**
     * Checks if this voucher is currently valid for use.
     * 
     * <p>A voucher is considered valid if it meets all of the following criteria:</p>
     * <ul>
     *   <li>Status is REDEEMED (not USED or EXPIRED)</li>
     *   <li>Current time is before the validTo timestamp</li>
     * </ul>
     * 
     * @return true if the voucher is valid for use, false otherwise
     */
    public boolean isValid() {
        return voucherStatus == VoucherStatus.REDEEMED && 
               LocalDateTime.now().isBefore(validTo);
    }

    /**
     * Checks if this voucher can be used in a transaction.
     * 
     * <p>This method combines the validity check with the status check to determine
     * if the voucher is ready for use.</p>
     * 
     * @return true if the voucher can be used, false otherwise
     */
    public boolean canUse() {
        return voucherStatus == VoucherStatus.REDEEMED && isValid();
    }

    /**
     * Marks this voucher as used after it has been applied to a transaction.
     * 
     * <p>This method validates that the voucher can be used before changing its status.
     * Once used, a voucher cannot be used again.</p>
     * 
     * @throws VoucherDomainException if the voucher cannot be used
     */
    public void use() {
        if (!canUse()) {
            throw new VoucherDomainException("Voucher cannot be used. Status: " + voucherStatus + ", Valid: " + isValid());
        }
        this.voucherStatus = VoucherStatus.USED;
    }

    /**
     * Automatically expires this voucher if it has passed its validity date.
     * 
     * <p>This method checks if the voucher is in REDEEMED status and has passed
     * its validTo timestamp, then updates the status to EXPIRED.</p>
     * 
     * <p>This method is typically called by scheduled tasks or during voucher validation.</p>
     */
    public void expire() {
        if (voucherStatus == VoucherStatus.REDEEMED && LocalDateTime.now().isAfter(validTo)) {
            this.voucherStatus = VoucherStatus.EXPIRED;
        }
    }

    /**
     * Checks if this voucher has expired.
     * 
     * <p>A voucher is considered expired if the current time is after its validTo timestamp,
     * regardless of its current status.</p>
     * 
     * @return true if the voucher has expired, false otherwise
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(validTo);
    }
}
