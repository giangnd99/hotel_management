package com.poly.producer;

import com.poly.message.BaseMessage;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;
import java.util.function.BiConsumer;

public interface KafkaJsonProducer<K extends Serializable, V extends BaseMessage> {

    void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback);
}
