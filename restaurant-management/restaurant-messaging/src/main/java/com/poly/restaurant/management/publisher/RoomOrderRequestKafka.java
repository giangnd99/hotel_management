package com.poly.restaurant.management.publisher;

import com.poly.restaurant.management.config.RestaurantConfigTopicData;
import com.poly.restaurant.management.helper.RoomOrderMessageHelper;

import com.poly.restaurant.management.mapper.RoomOrderMessageMapper;
import com.poly.restaurant.management.message.RoomOrderRequestMessage;
import com.poly.restaurant.management.message.RoomOrderRequestMessageAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.Callback;
import org.springframework.stereotype.Component;

/**
 * Kafka Publisher cho room order request messages
 * 
 * NGHIỆP VỤ:
 * - Gửi room order request messages đến Kafka
 * - Chuyển đổi domain message thành Avro message
 * - Xử lý callback và logging
 * 
 * PATTERNS ÁP DỤNG:
 * - Publisher Pattern: Gửi messages
 * - Mapper Pattern: Chuyển đổi message format
 * - Callback Pattern: Xử lý kết quả async
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoomOrderRequestKafka implements RoomOrderRequestPublisher {

    private final KafkaProducer<String, RoomOrderRequestMessageAvro> kafkaProducer;
    private final RoomOrderMessageMapper roomOrderMessageMapper;
    private final RestaurantConfigTopicData restaurantConfigTopicData;

    @Override
    public void publish(RoomOrderRequestMessage roomOrderRequestMessage) {
        validateInputParameters(roomOrderRequestMessage);
        logProcessingStart(roomOrderRequestMessage);

        try {
            RoomOrderRequestMessageAvro roomOrderRequestMessageAvro =
                    roomOrderMessageMapper.roomOrderRequestMessageToRoomOrderRequestMessageAvro(roomOrderRequestMessage);
            
            sendMessageToKafka(roomOrderRequestMessageAvro, roomOrderRequestMessage);
            logProcessingSuccess(roomOrderRequestMessage);
        } catch (Exception e) {
            handleProcessingError(roomOrderRequestMessage, e);
            throw new RuntimeException("Failed to publish room order request message", e);
        }
    }

    private void validateInputParameters(RoomOrderRequestMessage roomOrderRequestMessage) {
        if (!RoomOrderMessageHelper.isValidRoomOrderRequestMessage(roomOrderRequestMessage)) {
            throw new IllegalArgumentException("Invalid room order request message");
        }
    }

    private void sendMessageToKafka(RoomOrderRequestMessageAvro roomOrderRequestMessageAvro,
                                    RoomOrderRequestMessage roomOrderRequestMessage) {
        String topicName = restaurantConfigTopicData.getRestaurantRoomOrderRequestTopicName();
        String messageKey = roomOrderRequestMessage.getOrderId();

        ProducerRecord<String, RoomOrderRequestMessageAvro> record = 
                new ProducerRecord<>(topicName, messageKey, roomOrderRequestMessageAvro);

        kafkaProducer.send(record, new Callback() {
            @Override
            public void onCompletion(org.apache.kafka.clients.producer.RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    log.debug("Room order request message sent successfully to Kafka topic: {} with key: {}",
                            topicName, messageKey);
                } else {
                    log.error("Failed to send room order request message to Kafka topic: {} with key: {}. Error: {}",
                            topicName, messageKey, exception.getMessage(), exception);
                }
            }
        });
    }

    private void logProcessingStart(RoomOrderRequestMessage roomOrderRequestMessage) {
        log.info("Starting to publish room order request message for order: {} to room: {}",
                roomOrderRequestMessage.getOrderId(), roomOrderRequestMessage.getRoomId());
        RoomOrderMessageHelper.logRoomOrderRequestDetails(roomOrderRequestMessage);
    }

    private void logProcessingSuccess(RoomOrderRequestMessage roomOrderRequestMessage) {
        log.info("Successfully published room order request message for order: {} to room: {}",
                roomOrderRequestMessage.getOrderId(), roomOrderRequestMessage.getRoomId());
    }

    private void handleProcessingError(RoomOrderRequestMessage roomOrderRequestMessage, Exception e) {
        log.error("Error publishing room order request message for order: {} to room: {}. Error: {}",
                roomOrderRequestMessage.getOrderId(), roomOrderRequestMessage.getRoomId(), e.getMessage(), e);
        RoomOrderMessageHelper.logRoomOrderError("publish", roomOrderRequestMessage.getOrderId(), e.getMessage());
    }
}
