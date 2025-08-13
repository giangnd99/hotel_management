package com.poly.promotion.domain.core.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <h2>DomainEvent Base Class</h2>
 * 
 * <p>Abstract base class for all domain events in the promotion system.
 * Domain events represent significant occurrences or state changes that have
 * happened within the domain and may be of interest to other parts of the system.</p>
 * 
 * <p><strong>Purpose:</strong></p>
 * <ul>
 *   <li>Capture important business occurrences for audit and tracking</li>
 *   <li>Enable loose coupling between domain components</li>
 *   <li>Support event-driven architecture patterns</li>
 *   <li>Provide a foundation for event sourcing and CQRS</li>
 * </ul>
 * 
 * <p><strong>Event Properties:</strong></p>
 * <ul>
 *   <li><strong>Event ID:</strong> Unique identifier for each event instance</strong></li>
 *   <li><strong>Occurred On:</strong> Timestamp when the event occurred</strong></li>
 *   <li><strong>Event Type:</strong> Human-readable description of the event type</strong></li>
 * </ul>
 * 
 * <p><strong>Usage:</strong></p>
 * <p>This class should be extended by specific domain events that represent
 * business occurrences such as voucher creation, redemption, usage, or expiration.</p>
 * 
 * <p><strong>Event Publishing:</strong></p>
 * <p>Domain events are typically published by domain services or entities
 * and consumed by event handlers, external systems, or audit components.</p>
 * 
 * @author Nguyen Dam Hoang Linh
 * @version 1.0
 * @since 2025
 * @see VoucherPackCreatedEvent
 * @see VoucherRedeemedEvent
 */
public abstract class DomainEvent {
    
    /**
     * Unique identifier for this event instance.
     * Generated automatically when the event is created to ensure uniqueness
     * across the entire system.
     */
    private final UUID eventId;
    
    /**
     * Timestamp when this event occurred.
     * Captures the exact moment the business event happened, which is useful
     * for auditing, debugging, and temporal analysis.
     */
    private final LocalDateTime occurredOn;
    
    /**
     * Human-readable description of the event type.
     * Provides a clear, descriptive name for the event that can be used
     * for logging, monitoring, and debugging purposes.
     */
    private final String eventType;

    /**
     * Creates a new domain event with the specified event type.
     * 
     * <p>This constructor automatically generates a unique event ID and
     * sets the occurrence timestamp to the current time.</p>
     * 
     * @param eventType a descriptive name for the type of event
     * @throws IllegalArgumentException if eventType is null or empty
     */
    protected DomainEvent(String eventType) {
        if (eventType == null || eventType.trim().isEmpty()) {
            throw new IllegalArgumentException("Event type cannot be null or empty");
        }
        
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.eventType = eventType;
    }

    /**
     * Gets the unique identifier for this event instance.
     * 
     * <p>This ID is guaranteed to be unique across the entire system and
     * can be used for deduplication, tracking, and correlation purposes.</p>
     * 
     * @return the unique event identifier
     */
    public UUID getEventId() {
        return eventId;
    }

    /**
     * Gets the timestamp when this event occurred.
     * 
     * <p>This timestamp represents the exact moment the business event
     * happened and is useful for temporal analysis and auditing.</p>
     * 
     * @return the timestamp when the event occurred
     */
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    /**
     * Gets the human-readable description of the event type.
     * 
     * <p>This description provides a clear, descriptive name for the event
     * that can be used for logging, monitoring, and debugging purposes.</p>
     * 
     * @return the event type description
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Returns a string representation of this domain event.
     * 
     * <p>This method provides a human-readable format that includes the event type,
     * ID, and occurrence timestamp for debugging and logging purposes.</p>
     * 
     * @return a formatted string representation of the event
     */
    @Override
    public String toString() {
        return String.format("DomainEvent{eventType='%s', eventId=%s, occurredOn=%s}",
                eventType, eventId, occurredOn);
    }

    /**
     * Checks if this event occurred within a specified time period.
     * 
     * <p>This utility method is useful for filtering events based on when
     * they occurred, such as finding recent events or events within a
     * specific time window.</p>
     * 
     * @param since the reference time to check against
     * @return true if this event occurred after the specified time, false otherwise
     * @throws IllegalArgumentException if since is null
     */
    public boolean occurredAfter(LocalDateTime since) {
        if (since == null) {
            throw new IllegalArgumentException("Reference time cannot be null");
        }
        return occurredOn.isAfter(since);
    }

    /**
     * Checks if this event occurred before a specified time.
     * 
     * <p>This utility method is useful for filtering events based on when
     * they occurred, such as finding historical events or events before
     * a specific point in time.</p>
     * 
     * @param until the reference time to check against
     * @return true if this event occurred before the specified time, false otherwise
     * @throws IllegalArgumentException if until is null
     */
    public boolean occurredBefore(LocalDateTime until) {
        if (until == null) {
            throw new IllegalArgumentException("Reference time cannot be null");
        }
        return occurredOn.isBefore(until);
    }
}
