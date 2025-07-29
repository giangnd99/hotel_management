package com.poly.saga;

public enum SagaStatus {
    STARTED, SUCCEEDED, FAILED, COMPENSATED, COMPENSATING, PROCESSING
}
