package com.poly.kafka.producer.exception;

public class KafkaProductionException extends RuntimeException {
    public KafkaProductionException(String message) {
        super(message);
    }
}
