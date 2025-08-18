package com.poly.dedup;

public interface DedupStore {
    /**
     * Mark messageId as processed if not seen before.
     *
     * @param key       unique id (messageId or business idempotency key)
     * @param ttlMillis ttl for this key
     * @return true if it was NOT seen before and now marked (i.e. first time),
     * false if already seen (duplicate).
     */
    boolean markIfNotProcessed(String key, long ttlMillis);

    /**
     * Put response to cache for correlation id (so that if duplicate request comes we can return same response).
     */
    void putResponse(String correlationId, Object response, long ttlMillis);

    /**
     * Get cached response for correlation id (null if none)
     */
    <T> T getResponse(String correlationId, Class<T> clazz);
}
