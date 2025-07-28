package com.poly.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.domain.exception.DomainException;

import com.poly.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;

import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
public class KafkaMessageHelper {

    private final ObjectMapper objectMapper;

    public <T> T getEventPayload(String payload, Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (Exception e) {
            log.error("Could not read {} object!", outputType.getName(), e);
            throw new DomainException("Could not read " + outputType.getName() + " object!", e);
        }
    }

    public <T, U> BiConsumer<SendResult<String, T>, Throwable>
    getKafkaCallback(String responseTopicName, T avroModel, U outboxMessage,
                     BiConsumer<U, OutboxStatus> outboxCallback,
                     String orderId, String avroModelName) {
        return (result, ex) -> {
            if (ex == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successful response from Kafka for order id: {}" +
                                " Topic: {} Partition: {} Offset: {} Timestamp: {}",
                        orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());
                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
            } else {
                log.error("Error while sending {} with message: {} and outbox type: {} to topic {}",
                        avroModelName, avroModel.toString(), outboxMessage.getClass().getName(), responseTopicName, ex);
                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
            }
        };
    }
}
