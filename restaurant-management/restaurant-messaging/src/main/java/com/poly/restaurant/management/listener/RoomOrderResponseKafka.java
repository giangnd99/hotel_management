package com.poly.restaurant.management.listener;

import com.poly.restaurant.application.port.in.message.listener.RoomOrderResponseListener;
import com.poly.restaurant.management.helper.RoomOrderMessageHelper;

import com.poly.restaurant.management.mapper.RoomOrderMessageMapper;
import com.poly.restaurant.management.message.RoomOrderResponseMessage;
import com.poly.restaurant.management.message.RoomOrderResponseMessageAvro;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka Listener cho room order response messages
 * 
 * NGHIỆP VỤ:
 * - Nhận room order response messages từ Kafka
 * - Chuyển đổi Avro message thành domain message
 * - Route đến listener phù hợp dựa trên response status
 * 
 * PATTERNS ÁP DỤNG:
 * - Listener Pattern: Nhận messages
 * - Mapper Pattern: Chuyển đổi message format
 * - Router Pattern: Định tuyến messages
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoomOrderResponseKafka {

    private final RoomOrderMessageMapper roomOrderMessageMapper;
    private final RoomOrderResponseListener roomOrderResponseListener;

    @KafkaListener(topics = "${kafka.topic.room-order-response}", groupId = "${kafka.group-id}")
    public void receive(@Payload List<RoomOrderResponseMessageAvro> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        validateInputParameters(messages, keys, partitions, offsets);
        logBatchProcessingInfo(messages.size(), keys, partitions, offsets);
        processRoomOrderResponseMessages(messages);
    }

    private void validateInputParameters(List<RoomOrderResponseMessageAvro> messages,
                                        List<String> keys,
                                        List<Integer> partitions,
                                        List<Long> offsets) {
        if (messages == null || messages.isEmpty()) {
            log.warn("Received empty or null room order response messages");
            return;
        }

        if (keys == null || keys.size() != messages.size()) {
            log.warn("Keys list is null or size mismatch with messages");
        }

        if (partitions == null || partitions.size() != messages.size()) {
            log.warn("Partitions list is null or size mismatch with messages");
        }

        if (offsets == null || offsets.size() != messages.size()) {
            log.warn("Offsets list is null or size mismatch with messages");
        }
    }

    private void logBatchProcessingInfo(int messageCount, List<String> keys,
                                       List<Integer> partitions, List<Long> offsets) {
        log.info("Received {} room order response messages for processing", messageCount);
        
        if (keys != null && !keys.isEmpty()) {
            log.debug("Message keys: {}", keys);
        }
        
        if (partitions != null && !partitions.isEmpty()) {
            log.debug("Message partitions: {}", partitions);
        }
        
        if (offsets != null && !offsets.isEmpty()) {
            log.debug("Message offsets: {}", offsets);
        }
    }

    private void processRoomOrderResponseMessages(List<RoomOrderResponseMessageAvro> messages) {
        log.info("Processing {} room order response messages", messages.size());

        for (RoomOrderResponseMessageAvro messageAvro : messages) {
            try {
                processSingleRoomOrderMessage(messageAvro);
            } catch (Exception e) {
                log.error("Error processing room order response message: {}", messageAvro, e);
            }
        }

        log.info("Completed processing {} room order response messages", messages.size());
    }

    private void processSingleRoomOrderMessage(RoomOrderResponseMessageAvro messageAvro) {
        logPaymentProcessingStart(messageAvro);

        try {
            RoomOrderResponseMessage roomOrderResponseMessage =
                    roomOrderMessageMapper.roomOrderResponseMessageAvroToRoomOrderResponseMessage(messageAvro);

            if (!RoomOrderMessageHelper.isValidRoomOrderResponseMessage(roomOrderResponseMessage)) {
                log.error("Invalid room order response message: {}", roomOrderResponseMessage);
                return;
            }

            processRoomOrderByStatus(roomOrderResponseMessage);
            logPaymentProcessingSuccess(roomOrderResponseMessage);
        } catch (Exception e) {
            handleProcessingError(messageAvro, e);
        }
    }

    private void processRoomOrderByStatus(RoomOrderResponseMessage roomOrderResponseMessage) {
        String responseStatus = roomOrderResponseMessage.getResponseStatus();
        
        if (RoomOrderMessageHelper.isResponseSuccessful(responseStatus)) {
            roomOrderResponseListener.onRoomOrderSuccess(roomOrderResponseMessage);
        } else if (RoomOrderMessageHelper.isResponseFailed(responseStatus)) {
            roomOrderResponseListener.onRoomOrderFailure(roomOrderResponseMessage);
        } else {
            handleUnknownResponseStatus(roomOrderResponseMessage, responseStatus);
        }
    }

    private void handleUnknownResponseStatus(RoomOrderResponseMessage roomOrderResponseMessage, String responseStatus) {
        log.warn("Unknown room order response status: {} for order: {}. Treating as failure.",
                responseStatus, roomOrderResponseMessage.getOrderId());
        roomOrderResponseListener.onRoomOrderFailure(roomOrderResponseMessage);
    }

    private void logPaymentProcessingStart(RoomOrderResponseMessageAvro messageAvro) {
        log.debug("Starting to process room order response message: {}", messageAvro.getId());
    }

    private void logPaymentProcessingSuccess(RoomOrderResponseMessage roomOrderResponseMessage) {
        log.debug("Successfully processed room order response message for order: {} with status: {}",
                roomOrderResponseMessage.getOrderId(), roomOrderResponseMessage.getResponseStatus());
    }

    private void handleProcessingError(RoomOrderResponseMessageAvro messageAvro, Exception e) {
        log.error("Error processing room order response message: {}. Error: {}",
                messageAvro.getId(), e.getMessage(), e);
    }
}
