package com.poly.promotion.domain.core.event;

import com.poly.promotion.domain.core.valueobject.VoucherPackId;

/**
 * <h2>VoucherPackCreatedEvent Class</h2>
 * 
 * <p>Domain event that is raised when a new voucher pack is created in the promotion system.
 * This event captures the creation of a voucher pack and provides information about
 * the newly created pack for downstream consumers.</p>
 * 
 * <p><strong>Event Triggers:</strong></p>
 * <ul>
 *   <li>Administrator creates a new voucher pack through the management interface</li>
 *   <li>System creates voucher packs through automated processes</li>
 *   <li>Bulk import of voucher packs from external systems</li>
 * </ul>
 * 
 * <p><strong>Event Consumers:</strong></p>
 * <ul>
 *   <li>Audit and logging systems</li>
 *   <li>Notification services (e.g., email alerts to administrators)</li>
 *   <li>Analytics and reporting systems</li>
 *   <li>External integrations and webhooks</li>
 * </ul>
 * 
 * <p><strong>Business Context:</strong></p>
 * <p>This event is significant because it represents the introduction of new promotional
 * opportunities into the system. It may trigger various business processes such as
 * inventory tracking, marketing campaigns, or compliance checks.</p>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see DomainEvent
 * @see VoucherPackId
 */
public class VoucherPackCreatedEvent extends DomainEvent {
    
    /**
     * The unique identifier of the voucher pack that was created.
     * This ID can be used by event consumers to retrieve additional details
     * about the voucher pack or perform related operations.
     */
    private final VoucherPackId voucherPackId;

    /**
     * Creates a new VoucherPackCreatedEvent for the specified voucher pack.
     * 
     * <p>This constructor creates an event with a descriptive event type and
     * stores the voucher pack ID for reference by event consumers.</p>
     * 
     * @param voucherPackId the ID of the voucher pack that was created
     * @throws IllegalArgumentException if voucherPackId is null
     */
    public VoucherPackCreatedEvent(VoucherPackId voucherPackId) {
        super("VoucherPackCreated");
        
        if (voucherPackId == null) {
            throw new IllegalArgumentException("Voucher pack ID cannot be null");
        }
        
        this.voucherPackId = voucherPackId;
    }

    /**
     * Gets the unique identifier of the voucher pack that was created.
     * 
     * <p>This ID can be used by event consumers to:</p>
     * <ul>
     *   <li>Retrieve the full voucher pack details</li>
     *   <li>Perform related business operations</li>
     *   <li>Update external systems or databases</li>
     *   <li>Generate audit logs or reports</li>
     * </ul>
     * 
     * @return the voucher pack identifier
     */
    public VoucherPackId getVoucherPackId() {
        return voucherPackId;
    }

    /**
     * Returns a string representation of this event.
     * 
     * <p>This method provides a human-readable format that includes the event type,
     * voucher pack ID, and other relevant information for debugging and logging.</p>
     * 
     * @return a formatted string representation of the event
     */
    @Override
    public String toString() {
        return String.format("VoucherPackCreatedEvent{voucherPackId=%s, eventId=%s, occurredOn=%s}",
                voucherPackId, getEventId(), getOccurredOn());
    }
}
