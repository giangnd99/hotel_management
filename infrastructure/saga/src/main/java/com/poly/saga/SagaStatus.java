package com.poly.saga;

public enum SagaStatus {
    STARTED, PROCESSING, COMPENSATED, COMPENSATING, FINISHED, FAILED
}
