package com.poly.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@Builder
public class BaseMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String messageId;       // UUID unique per message
    private String correlationId;   // Correlate req/resp or saga
    private String sourceService;
    private String targetService;   // optional
    private long timestamp;         // epoch millis
    private int retryCount;
    private boolean duplicate;      // flag in case detected at sender/receiver
    private MessageType messageType;

    protected BaseMessage() {
        this.messageId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }
}
