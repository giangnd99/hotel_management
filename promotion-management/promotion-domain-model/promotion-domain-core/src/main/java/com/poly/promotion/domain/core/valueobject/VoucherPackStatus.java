package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * <h2>VoucherPackStatus Enum</h2>
 * 
 * <p>Represents the current status of a voucher pack in the promotion system.
 * This enum defines all possible states a voucher pack can be in during its lifecycle,
 * from creation to final closure or expiration.</p>
 * 
 * <p><strong>Status Lifecycle:</strong></p>
 * <ol>
 *   <li><strong>PENDING:</strong> Voucher pack is created but not yet available for redemption</li>
 *   <li><strong>PUBLISHED:</strong> Voucher pack is active and available for customer redemption</li>
 *   <li><strong>CLOSED:</strong> Voucher pack is manually closed or out of stock</li>
 *   <li><strong>EXPIRED:</strong> Voucher pack has passed its validity period</li>
 * </ol>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Only PUBLISHED packs can have vouchers redeemed from them</li>
 *   <li>PENDING packs can be modified before publication</li>
 *   <li>CLOSED packs cannot have new vouchers redeemed</li>
 *   <li>EXPIRED packs are automatically marked by the system</li>
 * </ul>
 * 
 * <p><strong>Status Transitions:</strong></p>
 * <ul>
 *   <li>PENDING → PUBLISHED: When pack is activated for customer use</li>
 *   <li>PUBLISHED → CLOSED: When pack is manually closed or stock reaches zero</li>
 *   <li>PUBLISHED → EXPIRED: When pack passes its validity date</li>
 *   <li>PENDING → EXPIRED: When pack passes its validity date before publication</li>
 *   <li>Any status → EXPIRED: System can expire packs from any status</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPack
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum VoucherPackStatus {
    
    /**
     * Voucher pack is created but not yet available for customer redemption.
     * This status allows administrators to configure and modify the pack
     * before making it available to customers.
     */
    PENDING,
    
    /**
     * Voucher pack is active and available for customer redemption.
     * This is the operational status that allows customers to exchange
     * loyalty points for vouchers from this pack.
     */
    PUBLISHED,
    
    /**
     * Voucher pack is manually closed or has exhausted its stock.
     * Once closed, no new vouchers can be redeemed from this pack,
     * but existing vouchers remain valid until their individual expiration.
     */
    CLOSED,
    
    /**
     * Voucher pack has passed its validity period and can no longer be used.
     * This status is automatically set by the system when packs expire,
     * regardless of their current status or stock level.
     */
    EXPIRED;

    /**
     * Creates a VoucherPackStatus from a string representation.
     * 
     * <p>This method performs case-insensitive matching to find the corresponding
     * enum value. It's useful for parsing status values from external sources
     * such as API requests, database records, or configuration files.</p>
     * 
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>"pending" → PENDING</li>
     *   <li>"PUBLISHED" → PUBLISHED</li>
     *   <li>"Closed" → CLOSED</li>
     *   <li>"expired" → EXPIRED</li>
     * </ul>
     * 
     * @param status the string representation of the status
     * @return the corresponding VoucherPackStatus enum value
     * @throws IllegalArgumentException if the string doesn't match any known status
     */
    public static VoucherPackStatus fromString(String status) {
        for (VoucherPackStatus voucherPackStatus : VoucherPackStatus.values()) {
            if (voucherPackStatus.name().equalsIgnoreCase(status)) {
                return voucherPackStatus;
            }
        }
        throw new IllegalArgumentException("Unknown VoucherPackStatus: " + status);
    }

    /**
     * Checks if a transition from the current status to a new status is valid.
     * 
     * <p>This method enforces business rules for status transitions, ensuring
     * that voucher packs follow the proper lifecycle flow.</p>
     * 
     * <p><strong>Valid Transitions:</strong></p>
     * <ul>
     *   <li>PENDING → PUBLISHED: Pack activation</li>
     *   <li>PENDING → CLOSED: Manual closure before publication</li>
     *   <li>PUBLISHED → CLOSED: Manual closure or stock exhaustion</li>
     *   <li>Any → EXPIRED: System expiration (always allowed)</li>
     * </ul>
     * 
     * @param newStatus the target status for the transition
     * @return true if the transition is valid, false otherwise
     */
    public boolean canTransitionTo(VoucherPackStatus newStatus) {
        if (newStatus == EXPIRED) {
            // System can expire packs from any status
            return true;
        }
        
        switch (this) {
            case PENDING:
                return newStatus == PUBLISHED || newStatus == CLOSED;
            case PUBLISHED:
                return newStatus == CLOSED;
            case CLOSED:
                // CLOSED is a terminal state - no further transitions allowed
                return false;
            case EXPIRED:
                // EXPIRED is a terminal state - no further transitions allowed
                return false;
            default:
                return false;
        }
    }

    /**
     * Checks if this status represents an active pack that can have vouchers redeemed.
     * 
     * <p>Only PUBLISHED packs are considered active and available for voucher redemption.
     * Other statuses (PENDING, CLOSED, EXPIRED) are not available for customer use.</p>
     * 
     * @return true if the pack status allows voucher redemption, false otherwise
     */
    public boolean isActive() {
        return this == PUBLISHED;
    }

    /**
     * Checks if this status represents a terminal state.
     * 
     * <p>Terminal states are those from which no further transitions are possible.
     * CLOSED and EXPIRED are terminal states as they represent the end of a pack's lifecycle.</p>
     * 
     * @return true if this is a terminal status, false otherwise
     */
    public boolean isTerminal() {
        return this == CLOSED || this == EXPIRED;
    }

    /**
     * Checks if this status allows modification of the voucher pack.
     * 
     * <p>Only PENDING packs can be modified. Once published, closed, or expired,
     * packs cannot be modified to maintain data integrity.</p>
     * 
     * @return true if the pack can be modified, false otherwise
     */
    public boolean isModifiable() {
        return this == PENDING;
    }
}
