package com.poly.kafka.producer;

import com.poly.kafka.producer.service.KafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;
import java.util.function.BiConsumer;

@Slf4j
public abstract class AbstractKafkaPublisher<K extends Serializable, V extends SpecificRecordBase, M> {

    protected abstract String getTopicName();

    protected abstract K getKey(M message);

    protected abstract V toAvro(M message);

    protected abstract String getMessageName();

    private final KafkaProducer<K, V> kafkaProducer;

    protected AbstractKafkaPublisher(KafkaProducer<K, V> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public void publish(M message) {
        try {
            V payload = toAvro(message);
            K key = getKey(message);

            kafkaProducer.send(
                    getTopicName(),
                    key,
                    payload,
                    callback(message, payload)
            );

        } catch (Exception e) {
            log.error("[{}] Unexpected error while publishing message: {}", getMessageName(), message, e);
        }
    }

    private BiConsumer<SendResult<K, V>, Throwable> callback(M message, V payload) {
        return (result, ex) -> {
            if (ex != null) {
                log.error("[{}] Failed to publish message: {} Payload: {}",
                        getMessageName(), message, payload, ex);
            } else {
                log.info("[{}] Successfully published message: {} to topic {} partition {} offset {}",
                        getMessageName(),
                        message,
                        result.getRecordMetadata().topic(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        };
    }
}
