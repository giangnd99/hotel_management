package com.poly.booking.management.messaging.publisher.kafka;

import com.poly.booking.management.domain.config.BookingServiceConfigData;
import com.poly.booking.management.domain.kafka.model.BookingRoomRequestAvro;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.booking.management.domain.port.out.message.publisher.RoomCheckInMessagePublisher;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.kafka.producer.KafkaMessageHelper;
import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.function.BiConsumer;

/**
 * Room Check Out Kafka Publisher
 * <p>
 * CHỨC NĂNG:
 * - Gửi room check out messages đến Kafka topic "room-check-out-topic"
 * - Xử lý outbox messages cho room check out process
 * - Đảm bảo tính nhất quán dữ liệu thông qua Outbox Pattern
 * <p>
 * MỤC ĐÍCH:
 * - Gửi thông tin checkout phòng đến room management service
 * - Xử lý callback để cập nhật trạng thái outbox messages
 * - Đảm bảo message delivery reliability
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Saga Pattern: Quản lý distributed transaction
 * - Domain Events: Tách biệt business logic
 * <p>
 * FLOW XỬ LÝ:
 * 1. Nhận room outbox message từ outbox service
 * 2. Validate và extract thông tin cần thiết
 * 3. Gửi message đến Kafka topic
 * 4. Cập nhật outbox status thông qua callback
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomCheckInKafkaPublisher implements RoomCheckInMessagePublisher {

    private final KafkaProducer<String, BookingRoomRequestAvro> kafkaProducer;
    private final BookingServiceConfigData bookingServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final BookingMessageDataMapper bookingDataMapper;

    @Override
    public void sendRoomCheckInRequest(RoomOutboxMessage roomOutboxMessage,
                                       BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {

        validateInputParameters(roomOutboxMessage, outboxCallback);

        ReservedEventPayload eventPayload = kafkaMessageHelper.getEventPayload(roomOutboxMessage.getPayload(), ReservedEventPayload.class);
        String sagaId = extractSagaId(roomOutboxMessage);

        logProcessingStart(roomOutboxMessage, sagaId);

        try {
            BookingRoomRequestAvro roomCheckOutRequestAvro = createRoomCheckOutRequestAvro(sagaId,eventPayload);

            sendMessageToKafka(roomCheckOutRequestAvro, sagaId, roomOutboxMessage, outboxCallback);

            logProcessingSuccess(roomOutboxMessage, sagaId);

        } catch (Exception e) {
            handleProcessingError(roomOutboxMessage, sagaId, e);
        }
    }

    private void validateInputParameters(RoomOutboxMessage roomOutboxMessage,
                                         BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {
        Assert.notNull(roomOutboxMessage, "RoomOutboxMessage không được null");
        Assert.notNull(outboxCallback, "OutboxCallback không được null");
        Assert.notNull(roomOutboxMessage.getPayload(), "RoomOutboxMessage payload không được null");
        Assert.notNull(roomOutboxMessage.getSagaId(), "RoomOutboxMessage sagaId không được null");
        Assert.hasText(roomOutboxMessage.getType(), "RoomOutboxMessage type không được empty");
    }

    private String extractSagaId(RoomOutboxMessage roomOutboxMessage) {
        return roomOutboxMessage.getSagaId().toString();
    }

    private void logProcessingStart(RoomOutboxMessage roomOutboxMessage, String sagaId) {
        log.info("Bắt đầu xử lý RoomCheckOutRequest cho booking: {}, saga id: {}",
                roomOutboxMessage.getBookingId(),
                sagaId);
    }

    private BookingRoomRequestAvro createRoomCheckOutRequestAvro(String sagaId, ReservedEventPayload eventPayload) {
        return bookingDataMapper.bookingRoomCheckInEventToRoomRequestAvroModel(sagaId,eventPayload);
    }

    private void sendMessageToKafka(BookingRoomRequestAvro roomCheckOutRequestAvro,
                                    String sagaId,
                                    RoomOutboxMessage roomOutboxMessage,
                                    BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {

        String topicName = bookingServiceConfigData.getRoomCheckInTopicName();

        kafkaProducer.send(
                topicName,
                sagaId,
                roomCheckOutRequestAvro,
                kafkaMessageHelper.getKafkaCallback(
                        topicName,
                        roomCheckOutRequestAvro,
                        roomOutboxMessage,
                        outboxCallback,
                        roomOutboxMessage.getBookingId().toString(),
                        "RoomCheckOutRequestAvroModel"
                )
        );
    }

    private void logProcessingSuccess(RoomOutboxMessage roomOutboxMessage, String sagaId) {
        log.info("RoomCheckOutRequest đã được gửi thành công đến Kafka cho booking: {}, saga id: {}",
                roomOutboxMessage.getBookingId(),
                sagaId);
    }

    private void handleProcessingError(RoomOutboxMessage roomOutboxMessage,
                                       String sagaId,
                                       Exception exception) {
        log.error("Lỗi khi gửi RoomCheckOutRequest đến Kafka với booking: {}, saga id: {}. Lỗi: {}",
                roomOutboxMessage.getBookingId(),
                sagaId,
                exception.getMessage(),
                exception);
    }
}
