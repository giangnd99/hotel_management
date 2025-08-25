package com.poly.booking.management.messaging.publisher.kafka;

import com.poly.booking.management.domain.config.BookingServiceConfigData;
import com.poly.booking.management.domain.kafka.model.BookingRoomRequestAvro;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.port.out.message.publisher.RoomRequestReserveMessagePublisher;
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
 * Kafka Publisher chịu trách nhiệm gửi yêu cầu đặt phòng (room reservation request)
 * đến Kafka topic để xử lý việc đặt phòng trong hệ thống khách sạn.
 * <p>
 * NGHIỆP VỤ:
 * - Gửi thông tin đặt phòng bao gồm: roomId, roomNumber, roomType, price, capacity
 * - Đảm bảo tính nhất quán dữ liệu thông qua Outbox Pattern
 * - Xử lý bất đồng bộ cho việc đặt phòng
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Saga Pattern: Quản lý distributed transaction cho booking process
 * - Domain Events: Tách biệt business logic
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomRequestKafkaPublisher implements RoomRequestReserveMessagePublisher {

    private final BookingMessageDataMapper bookingDataMapper;
    private final KafkaProducer<String, BookingRoomRequestAvro> kafkaProducer;
    private final BookingServiceConfigData bookingServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    public void sendRoomReserveRequest(RoomOutboxMessage roomOutboxMessage,
                                       BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {

        validateInputParameters(roomOutboxMessage, outboxCallback);

        ReservedEventPayload roomEventPayload = extractRoomEventPayload(roomOutboxMessage);

        String sagaId = extractSagaId(roomOutboxMessage);

        logProcessingStart(roomEventPayload, sagaId);

        try {
            BookingRoomRequestAvro roomRequestAvro = createRoomRequestAvro(sagaId, roomEventPayload);

            sendMessageToKafka(roomRequestAvro, sagaId, roomOutboxMessage, outboxCallback, roomEventPayload);

            logProcessingSuccess(roomEventPayload, sagaId);

        } catch (Exception e) {
            handleProcessingError(roomEventPayload, sagaId, e);
        }
    }

    private void validateInputParameters(RoomOutboxMessage roomOutboxMessage,
                                         BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {
        Assert.notNull(roomOutboxMessage, "BookingRoomOutboxMessage không được null");
        Assert.notNull(outboxCallback, "OutboxCallback không được null");
        Assert.notNull(roomOutboxMessage.getPayload(), "BookingRoomOutboxMessage payload không được null");
        Assert.notNull(roomOutboxMessage.getSagaId(), "BookingRoomOutboxMessage sagaId không được null");
        Assert.hasText(roomOutboxMessage.getBookingId().toString(), "BookingRoomOutboxMessage bookingId không được empty");
    }


    private ReservedEventPayload extractRoomEventPayload(RoomOutboxMessage roomOutboxMessage) {
        return kafkaMessageHelper.getEventPayload(
                roomOutboxMessage.getPayload(),
                ReservedEventPayload.class
        );
    }


    private String extractSagaId(RoomOutboxMessage roomOutboxMessage) {
        return roomOutboxMessage.getSagaId().toString();
    }

    private void logProcessingStart(ReservedEventPayload roomEventPayload, String sagaId) {
        log.info("Bắt đầu xử lý RoomReservationRequest cho booking: {} , saga id: {}",
                roomEventPayload.getBookingId(),
                sagaId);
    }

    private BookingRoomRequestAvro createRoomRequestAvro(String sagaId,
                                                         ReservedEventPayload roomEventPayload) {
        return bookingDataMapper.bookingRoomEventToRoomRequestAvroModel(sagaId, roomEventPayload);
    }

    private void sendMessageToKafka(BookingRoomRequestAvro roomRequestAvro,
                                    String sagaId,
                                    RoomOutboxMessage roomOutboxMessage,
                                    BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback,
                                    ReservedEventPayload roomEventPayload) {

        String topicName = bookingServiceConfigData.getRoomReserveRequestTopicName();

        kafkaProducer.send(
                topicName,
                sagaId,
                roomRequestAvro,
                kafkaMessageHelper.getKafkaCallback(
                        topicName,
                        roomRequestAvro,
                        roomOutboxMessage,
                        outboxCallback,
                        roomEventPayload.getBookingId(),
                        "RoomReservationRequestAvroModel"
                )
        );
    }


    private void logProcessingSuccess(ReservedEventPayload roomEventPayload, String sagaId) {
        log.info("RoomReservationRequest đã được gửi thành công đến Kafka cho room với booking id: {} , saga id: {}",
                roomEventPayload.getBookingId(),
                sagaId);
    }

    private void handleProcessingError(ReservedEventPayload roomEventPayload,
                                       String sagaId,
                                       Exception exception) {
        log.error("Lỗi khi gửi RoomReservationRequest đến Kafka với booking id: {} , saga id: {}. Lỗi: {}",
                roomEventPayload.getBookingId(),
                sagaId,
                exception.getMessage(),
                exception);
    }
}
