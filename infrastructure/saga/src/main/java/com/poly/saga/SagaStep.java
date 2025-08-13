package com.poly.saga;


/**
 * Contract chung cho tất cả Saga Step.
 * Mỗi Saga Step phải định nghĩa được cách xử lý chính (process) và rollback khi thất bại.
 */
public interface SagaStep<T> {

    /**
     * Xử lý chính của Saga Step.
     *
     * @param data dữ liệu message/event từ Kafka hoặc từ service gọi
     * @return SagaStatus mới sau khi xử lý
     */
    void process(T data);

    /**
     * Xử lý rollback (compensating transaction) khi Saga thất bại.
     *
     * @param data dữ liệu message/event từ Kafka hoặc từ service gọi
     * @return SagaStatus mới sau khi rollback
     */
    void rollback(T data);

}
