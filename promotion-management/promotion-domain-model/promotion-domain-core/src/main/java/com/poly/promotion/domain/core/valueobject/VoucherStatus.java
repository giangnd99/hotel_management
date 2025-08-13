package com.poly.promotion.domain.core.valueobject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * <h2>VoucherStatus Enum</h2>
 * 
 * <p>Represents the current status of a voucher in the promotion system.
 * This enum defines all possible states a voucher can be in during its lifecycle,
 * from creation to final usage or expiration.</p>
 * 
 * <p><strong>Status Lifecycle:</strong></p>
 * <ol>
 *   <li><strong>PENDING:</strong> Voucher is created but not yet available for use</li>
 *   <li><strong>REDEEMED:</strong> Voucher is available for use by the customer</li>
 *   <li><strong>USED:</strong> Voucher has been applied to a transaction</li>
 *   <li><strong>EXPIRED:</strong> Voucher has passed its validity period</li>
 * </ol>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Only REDEEMED vouchers can be used in transactions</li>
 *   <li>USED vouchers cannot be used again</li>
 *   <li>EXPIRED vouchers are automatically marked by the system</li>
 *   <li>Status transitions follow a specific flow for data integrity</li>
 * </ul>
 * 
 * <p><strong>Status Transitions:</strong></p>
 * <ul>
 *   <li>PENDING → REDEEMED: When voucher is activated</li>
 *   <li>REDEEMED → USED: When voucher is applied to a transaction</li>
 *   <li>REDEEMED → EXPIRED: When voucher passes its validity date</li>
 *   <li>Any status → EXPIRED: System can expire vouchers from any status</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see Voucher
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum VoucherStatus {
    
    /**
     * Voucher is created but not yet available for use.
     * This status is typically set when a voucher is first created during redemption
     * from a voucher pack, but before it's activated for customer use.
     */
    PENDING,
    
    /**
     * Voucher is available for use by the customer.
     * This is the active status that allows customers to apply the voucher
     * to transactions within its validity period.
     */
    REDEEMED,
    
    /**
     * Voucher has been successfully applied to a transaction.
     * Once a voucher reaches this status, it cannot be used again.
     * This represents the final state for successfully used vouchers.
     */
    USED,
    
    /**
     * Voucher has passed its validity period and can no longer be used.
     * This status is automatically set by the system when vouchers expire,
     * regardless of their current status.
     */
    EXPIRED;

    /**
     * Creates a VoucherStatus from a string representation.
     * 
     * <p>This method performs case-insensitive matching to find the corresponding
     * enum value. It's useful for parsing status values from external sources
     * such as API requests or database records.</p>
     * 
     * <p><strong>Examples:</strong></p>
     * <ul>
     *   <li>"pending" → PENDING</li>
     *   <li>"REDEEMED" → REDEEMED</li>
     *   <li>"Used" → USED</li>
     *   <li>"expired" → EXPIRED</li>
     * </ul>
     * 
     * @param status the string representation of the status
     * @return the corresponding VoucherStatus enum value
     * @throws IllegalArgumentException if the string doesn't match any known status
     */
    public static VoucherStatus fromString(String status) {
        for (VoucherStatus voucherStatus : VoucherStatus.values()) {
            if (voucherStatus.name().equalsIgnoreCase(status)) {
                return voucherStatus;
            }
        }
        throw new IllegalArgumentException("Unknown VoucherStatus: " + status);
    }

    /**
     * Checks if a transition from the current status to a new status is valid.
     * 
     * <p>This method enforces business rules for status transitions, ensuring
     * that vouchers follow the proper lifecycle flow.</p>
     * 
     * <p><strong>Valid Transitions:</strong></p>
     * <ul>
     *   <li>PENDING → REDEEMED: Voucher activation</li>
     *   <li>REDEEMED → USED: Voucher usage</li>
     *   <li>REDEEMED → EXPIRED: Voucher expiration</li>
     *   <li>Any → EXPIRED: System expiration (always allowed)</li>
     * </ul>
     * 
     * @param newStatus the target status for the transition
     * @return true if the transition is valid, false otherwise
     */
    public boolean canTransitionTo(VoucherStatus newStatus) {
        if (newStatus == EXPIRED) {
            // System can expire vouchers from any status
            return true;
        }
        
        switch (this) {
            case PENDING:
                return newStatus == REDEEMED;
            case REDEEMED:
                return newStatus == USED;
            case USED:
                // USED is a terminal state - no further transitions allowed
                return false;
            case EXPIRED:
                // EXPIRED is a terminal state - no further transitions allowed
                return false;
            default:
                return false;
        }
    }

    /**
     * Checks if this status represents an active voucher that can be used.
     * 
     * <p>Only REDEEMED vouchers are considered active and available for use.
     * Other statuses (PENDING, USED, EXPIRED) are not available for transactions.</p>
     * 
     * @return true if the voucher status allows usage, false otherwise
     */
    public boolean isActive() {
        return this == REDEEMED;
    }

    /**
     * Checks if this status represents a terminal state.
     * 
     * <p>Terminal states are those from which no further transitions are possible.
     * USED and EXPIRED are terminal states as they represent the end of a voucher's lifecycle.</p>
     * 
     * @return true if this is a terminal status, false otherwise
     */
    public boolean isTerminal() {
        return this == USED || this == EXPIRED;
    }
}
