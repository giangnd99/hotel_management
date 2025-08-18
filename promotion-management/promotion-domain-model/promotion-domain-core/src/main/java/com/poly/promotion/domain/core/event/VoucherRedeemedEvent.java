package com.poly.promotion.domain.core.event;

import com.poly.domain.valueobject.CustomerId;
import com.poly.promotion.domain.core.valueobject.VoucherId;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;

/**
 * <h2>VoucherRedeemedEvent Class</h2>
 * 
 * <p>Domain event that is raised when a customer successfully redeems a voucher
 * from a voucher pack using their loyalty points. This event captures the successful
 * redemption transaction and provides information about the customer, voucher, and pack.</p>
 * 
 * <p><strong>Event Triggers:</strong></p>
 * <ul>
 *   <li>Customer successfully exchanges loyalty points for a voucher</li>
 *   <li>System processes a voucher redemption request</li>
 *   <li>Bulk voucher redemption operations</li>
 * </ul>
 * 
 * <p><strong>Event Consumers:</strong></p>
 * <ul>
 *   <li>Loyalty point management systems</li>
 *   <li>Customer notification services</li>
 *   <li>Analytics and reporting systems</li>
 *   <li>Audit and compliance systems</li>
 *   <li>External loyalty program integrations</li>
 * </ul>
 * 
 * <p><strong>Business Context:</strong></p>
 * <p>This event is significant because it represents a successful customer engagement
 * with the loyalty program. It may trigger various business processes such as:</p>
 * <ul>
 *   <li>Deduction of loyalty points from customer account</li>
 *   <li>Generation of confirmation emails or SMS</li>
 *   <li>Update of customer engagement metrics</li>
 *   <li>Triggering of follow-up marketing campaigns</li>
 * </ul>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see DomainEvent
 * @see VoucherId
 * @see VoucherPackId
 * @see CustomerId
 */
public class VoucherRedeemedEvent extends DomainEvent {
    
    /**
     * The unique identifier of the voucher that was redeemed.
     * This ID can be used by event consumers to retrieve additional details
     * about the voucher or perform related operations.
     */
    private final VoucherId voucherId;
    
    /**
     * The unique identifier of the customer who redeemed the voucher.
     * This ID can be used to identify the customer for notifications, analytics,
     * or other customer-specific operations.
     */
    private final CustomerId customerId;
    
    /**
     * The unique identifier of the voucher pack from which the voucher was redeemed.
     * This ID provides context about the source of the voucher and can be used
     * to retrieve pack details or perform pack-related operations.
     */
    private final VoucherPackId voucherPackId;
    
    /**
     * The number of loyalty points required to redeem this voucher.
     * This value can be used by event consumers to track point consumption,
     * generate reports, or perform loyalty-related calculations.
     */
    private final Long requiredPoints;

    /**
     * Creates a new VoucherRedeemedEvent for the specified voucher redemption.
     * 
     * <p>This constructor creates an event with a descriptive event type and
     * stores the relevant IDs and points information for reference by event consumers.</p>
     * 
     * @param voucherId the ID of the voucher that was redeemed
     * @param customerId the ID of the customer who redeemed the voucher
     * @param voucherPackId the ID of the voucher pack the voucher came from
     * @param requiredPoints the number of loyalty points required for redemption
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public VoucherRedeemedEvent(VoucherId voucherId, CustomerId customerId, VoucherPackId voucherPackId, Long requiredPoints) {
        super("VoucherRedeemed");
        
        if (voucherId == null) {
            throw new IllegalArgumentException("Voucher ID cannot be null");
        }
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (voucherPackId == null) {
            throw new IllegalArgumentException("Voucher pack ID cannot be null");
        }
        if (requiredPoints == null) {
            throw new IllegalArgumentException("Required points cannot be null");
        }
        
        this.voucherId = voucherId;
        this.customerId = customerId;
        this.voucherPackId = voucherPackId;
        this.requiredPoints = requiredPoints;
    }

    /**
     * Gets the unique identifier of the voucher that was redeemed.
     * 
     * <p>This ID can be used by event consumers to:</p>
     * <ul>
     *   <li>Retrieve the full voucher details</li>
     *   <li>Track voucher usage and lifecycle</li>
     *   <li>Generate voucher-specific reports</li>
     *   <li>Perform voucher-related operations</li>
     * </ul>
     * 
     * @return the voucher identifier
     */
    public VoucherId getVoucherId() {
        return voucherId;
    }

    /**
     * Gets the unique identifier of the customer who redeemed the voucher.
     * 
     * <p>This ID can be used by event consumers to:</p>
     * <ul>
     *   <li>Send confirmation notifications</li>
     *   <li>Update customer loyalty metrics</li>
     *   <li>Generate customer-specific reports</li>
     *   <li>Trigger personalized marketing campaigns</li>
     * </ul>
     * 
     * @return the customer identifier
     */
    public CustomerId getCustomerId() {
        return customerId;
    }

    /**
     * Gets the unique identifier of the voucher pack from which the voucher was redeemed.
     * 
     * <p>This ID can be used by event consumers to:</p>
     * <ul>
     *   <li>Retrieve pack details and configuration</li>
     *   <li>Update pack inventory and statistics</li>
     *   <li>Generate pack-specific analytics</li>
     *   <li>Perform pack-related business operations</li>
     * </ul>
     * 
     * @return the voucher pack identifier
     */
    public VoucherPackId getVoucherPackId() {
        return voucherPackId;
    }

    /**
     * Gets the number of loyalty points required to redeem this voucher.
     * 
     * <p>This value can be used by event consumers to:</p>
     * <ul>
     *   <li>Track point consumption and usage patterns</li>
     *   <li>Generate loyalty program reports</li>
     *   <li>Calculate customer engagement metrics</li>
     *   <li>Perform loyalty-related business analysis</li>
     * </ul>
     * 
     * @return the number of loyalty points required
     */
    public Long getRequiredPoints() {
        return requiredPoints;
    }

    /**
     * Returns a string representation of this event.
     * 
     * <p>This method provides a human-readable format that includes the event type,
     * voucher ID, customer ID, pack ID, required points, and other relevant information
     * for debugging and logging purposes.</p>
     * 
     * @return a formatted string representation of the event
     */
    @Override
    public String toString() {
        return String.format("VoucherRedeemedEvent{voucherId=%s, customerId=%s, voucherPackId=%s, requiredPoints=%d, eventId=%s, occurredOn=%s}",
                voucherId, customerId, voucherPackId, requiredPoints, getEventId(), getOccurredOn());
    }
}
