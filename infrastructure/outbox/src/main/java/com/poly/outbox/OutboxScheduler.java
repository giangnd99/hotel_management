package com.poly.outbox;

public interface OutboxScheduler {
    void processOutboxMessage();
}
