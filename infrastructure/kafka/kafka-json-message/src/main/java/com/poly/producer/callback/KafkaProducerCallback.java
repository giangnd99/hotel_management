package com.poly.producer.callback;

import com.poly.message.BaseMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.function.BiConsumer;

@Slf4j
@Component
public class KafkaProducerCallback<K extends Serializable, V extends BaseMessage> implements BiConsumer<SendResult<K, V>, Throwable> {

    private final String topicName;
    private final K key;
    private final V message;

    public KafkaProducerCallback(String topicName, K key, V message) {
        this.topicName = topicName;
        this.key = key;
        this.message = message;
    }


    @Override
    public void accept(SendResult<K, V> result, Throwable ex) {
        if (ex != null) {
            handleFailure(ex);
        } else {
            handleSuccess(result);
        }
    }

    private void handleSuccess(SendResult<K, V> result) {
        log.info("Message sent successfully! Topic: {}, Key: {}, Partition: {}, Offset: {}, Message: {}",
                topicName,
                key,
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset(),
                message);
    }

    private void handleFailure(Throwable ex) {
        log.error("Failed to send message! Topic: {}, Key: {}, Message: {}. Exception: {}",
                topicName,
                key,
                message,
                ex.getMessage(),
                ex);
    }
}
