package com.poly.promotion.domain.core.event;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private final String eventType;

    protected DomainEvent(String eventType) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.eventType = eventType;
    }

    public UUID getEventId() {
        return eventId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    public String getEventType() {
        return eventType;
    }
}
