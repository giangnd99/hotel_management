package com.poly.restaurant.management.publisher;

import com.poly.config.KafkaTopicsConfig;
import com.poly.message.model.room.RoomRequestMessage;
import com.poly.producer.KafkaJsonProducer;
import com.poly.producer.callback.KafkaProducerCallback;
import com.poly.restaurant.application.port.out.message.publisher.RoomRequestPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomPublisherKafka implements RoomRequestPublisher {

    private final KafkaJsonProducer<String, RoomRequestMessage> kafkaJsonProducer;

    public void publishRoomRequest(RoomRequestMessage message) {
        log.info("Publishing direct room request for order {} to topic {}", message.getCorrelationId(), "room-request");

        var callback = new KafkaProducerCallback<>(KafkaTopicsConfig.ROOM_APPROVAL_REQUEST_TOPIC,
                message.getCorrelationId(),
                message);

        kafkaJsonProducer.send(KafkaTopicsConfig.ROOM_APPROVAL_REQUEST_TOPIC,
                message.getCorrelationId(),
                message,
                callback);
    }

    @Override
    public void publish(RoomRequestMessage roomRequestMessage) {
        roomRequestMessage.setSourceService("restaurant-management");
        publishRoomRequest(roomRequestMessage);
        log.info("Room request published for order with id: {}", roomRequestMessage.getCorrelationId());
    }
}
