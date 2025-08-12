package com.poly.booking.management.messaging.publisher.kafka;

import com.poly.booking.management.domain.config.BookingServiceConfigData;
import com.poly.booking.management.domain.kafka.model.BookingRoomRequestAvro;
import com.poly.booking.management.domain.outbox.payload.RoomEventPayload;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.port.out.message.publisher.room.RoomRequestReserveMessagePublisher;
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

    /**
     * Gửi yêu cầu đặt phòng đến Kafka topic
     * <p>
     * NGHIỆP VỤ:
     * - Xử lý yêu cầu đặt phòng từ booking system
     * - Gửi thông tin phòng (room details) đến room management service
     * - Đảm bảo tính nhất quán dữ liệu thông qua outbox pattern
     *
     * @param roomOutboxMessage Message chứa thông tin đặt phòng từ outbox
     * @param outboxCallback    Callback function để cập nhật trạng thái outbox
     */
    @Override
    public void sendRoomReserveRequest(RoomOutboxMessage roomOutboxMessage,
                                       BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {

        // Validate input parameters
        validateInputParameters(roomOutboxMessage, outboxCallback);

        // Extract và parse thông tin từ outbox message
        RoomEventPayload roomEventPayload = extractRoomEventPayload(roomOutboxMessage);
        String sagaId = extractSagaId(roomOutboxMessage);

        // Log thông tin bắt đầu xử lý
        logProcessingStart(roomEventPayload, sagaId);

        try {
            // Tạo Avro model từ room event
            BookingRoomRequestAvro roomRequestAvro = createRoomRequestAvro(sagaId, roomEventPayload);

            // Gửi message đến Kafka
            sendMessageToKafka(roomRequestAvro, sagaId, roomOutboxMessage, outboxCallback, roomEventPayload);

            // Log thành công
            logProcessingSuccess(roomEventPayload, sagaId);

        } catch (Exception e) {
            // Log và xử lý lỗi
            handleProcessingError(roomEventPayload, sagaId, e);
        }
    }

    /**
     * Validate các tham số đầu vào
     * <p>
     * Đảm bảo tất cả thông tin cần thiết cho việc đặt phòng đều hợp lệ
     */
    private void validateInputParameters(RoomOutboxMessage roomOutboxMessage,
                                         BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {
        Assert.notNull(roomOutboxMessage, "BookingRoomOutboxMessage không được null");
        Assert.notNull(outboxCallback, "OutboxCallback không được null");
        Assert.notNull(roomOutboxMessage.getPayload(), "BookingRoomOutboxMessage payload không được null");
        Assert.notNull(roomOutboxMessage.getSagaId(), "BookingRoomOutboxMessage sagaId không được null");
        Assert.hasText(roomOutboxMessage.getBookingId(), "BookingRoomOutboxMessage bookingId không được empty");
    }

    /**
     * Trích xuất và parse BookingRoomEventPayload từ outbox message
     * <p>
     * Chứa thông tin chi tiết về phòng cần đặt:
     * - roomId: ID của phòng
     * - roomNumber: Số phòng
     * - roomTypeName: Loại phòng
     * - basePrice: Giá cơ bản
     * - capacity: Sức chứa
     */
    private RoomEventPayload extractRoomEventPayload(RoomOutboxMessage roomOutboxMessage) {
        return kafkaMessageHelper.getEventPayload(
                roomOutboxMessage.getPayload(),
                RoomEventPayload.class
        );
    }

    /**
     * Trích xuất Saga ID từ outbox message
     * <p>
     * Saga ID được sử dụng để theo dõi quy trình đặt phòng
     * và đảm bảo tính nhất quán trong distributed transaction
     */
    private String extractSagaId(RoomOutboxMessage roomOutboxMessage) {
        return roomOutboxMessage.getSagaId().toString();
    }

    /**
     * Log thông tin bắt đầu xử lý room reservation request
     * <p>
     * Ghi log chi tiết về việc bắt đầu xử lý đặt phòng
     */
    private void logProcessingStart(RoomEventPayload roomEventPayload, String sagaId) {
        log.info("Bắt đầu xử lý RoomReservationRequest cho room: {} , saga id: {}",
                roomEventPayload.getRoomNumber(),
                sagaId);
    }

    /**
     * Tạo BookingRoomRequestAvro model từ thông tin room event
     * <p>
     * Chuyển đổi domain event thành Avro model để gửi qua Kafka
     */
    private BookingRoomRequestAvro createRoomRequestAvro(String sagaId,
                                                         RoomEventPayload roomEventPayload) {
        return bookingDataMapper.bookingRoomEventToRoomRequestAvroModel(sagaId, roomEventPayload);
    }

    /**
     * Gửi message đến Kafka topic
     * <p>
     * Gửi thông tin đặt phòng đến room management service
     * để xử lý việc đặt phòng thực tế
     */
    private void sendMessageToKafka(BookingRoomRequestAvro roomRequestAvro,
                                    String sagaId,
                                    RoomOutboxMessage roomOutboxMessage,
                                    BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback,
                                    RoomEventPayload roomEventPayload) {

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
                        roomEventPayload.getRoomId(),
                        "RoomReservationRequestAvroModel"
                )
        );
    }

    /**
     * Log thông tin xử lý thành công
     * <p>
     * Ghi log khi việc gửi yêu cầu đặt phòng thành công
     */
    private void logProcessingSuccess(RoomEventPayload roomEventPayload, String sagaId) {
        log.info("RoomReservationRequest đã được gửi thành công đến Kafka cho room: {} , saga id: {}",
                roomEventPayload.getRoomNumber(),
                sagaId);
    }

    /**
     * Xử lý lỗi khi gửi message
     * <p>
     * Ghi log chi tiết lỗi và có thể thêm logic retry hoặc dead letter queue
     * để đảm bảo tính reliability của hệ thống đặt phòng
     */
    private void handleProcessingError(RoomEventPayload roomEventPayload,
                                       String sagaId,
                                       Exception exception) {
        log.error("Lỗi khi gửi RoomReservationRequest đến Kafka với room: {} , saga id: {}. Lỗi: {}",
                roomEventPayload.getRoomNumber(),
                sagaId,
                exception.getMessage(),
                exception);

        // TODO: Có thể thêm logic retry hoặc dead letter queue ở đây
        // TODO: Có thể gửi notification đến admin về lỗi đặt phòng
        // throw new RoomReservationPublishingException("Không thể gửi room reservation request", exception);
    }
}
