package com.poly.producer.impl;

import com.poly.message.BaseMessage;
import com.poly.producer.KafkaJsonProducer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaJsonProducerImpl<K extends Serializable, V extends BaseMessage> implements KafkaJsonProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback) {
        log.info("Sending message {} to topic: {} ", message, topicName);
        try {
            CompletableFuture<SendResult<K, V>> future = kafkaTemplate.send(topicName, key, message);
            future.whenComplete(callback);
        } catch (KafkaException e) {
            log.error("Error on kafka producer with key: {}, message: {} and exception: {}", key, message,
                    e.getMessage());
            throw new KafkaException("Error on kafka producer with key: " + key + " and message: " + message, e);
        }
    }

    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Closing Kafka producer");
            kafkaTemplate.destroy();
        }
    }
}
