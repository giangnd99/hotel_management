package com.poly.booking.management.messaging.publisher.kafka;

import com.poly.booking.management.domain.config.BookingServiceConfigData;
import com.poly.booking.management.domain.kafka.model.BookingRoomRequestAvro;
import com.poly.booking.management.domain.mapper.RoomDataMapper;
import com.poly.booking.management.domain.outbox.model.RoomOutboxMessage;
import com.poly.booking.management.domain.outbox.payload.ReservedEventPayload;
import com.poly.booking.management.domain.port.out.message.publisher.RoomCheckOutMessagePublisher;
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
public class RoomCheckInKafkaPublisher implements RoomCheckOutMessagePublisher {

    private final KafkaProducer<String, BookingRoomRequestAvro> kafkaProducer;
    private final BookingServiceConfigData bookingServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;
    private final BookingMessageDataMapper bookingDataMapper;

    /**
     * Gửi room check out request đến Kafka topic
     * <p>
     * NGHIỆP VỤ:
     * - Xử lý room check out request từ outbox service
     * - Gửi thông tin checkout phòng đến room management service
     * - Đảm bảo tính nhất quán dữ liệu thông qua outbox pattern
     *
     * @param roomOutboxMessage Message chứa thông tin checkout phòng từ outbox
     * @param outboxCallback    Callback function để cập nhật trạng thái outbox
     */
    @Override
    public void sendRoomCheckInRequest(RoomOutboxMessage roomOutboxMessage,
                                       BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {

        // Validate input parameters
        validateInputParameters(roomOutboxMessage, outboxCallback);

        ReservedEventPayload eventPayload = kafkaMessageHelper.getEventPayload(roomOutboxMessage.getPayload(), ReservedEventPayload.class);
        // Extract thông tin cần thiết
        String sagaId = extractSagaId(roomOutboxMessage);

        // Log thông tin bắt đầu xử lý
        logProcessingStart(roomOutboxMessage, sagaId);

        try {
            // Tạo Avro model từ room outbox message
            BookingRoomRequestAvro roomCheckOutRequestAvro = createRoomCheckOutRequestAvro(sagaId,eventPayload);

            // Gửi message đến Kafka
            sendMessageToKafka(roomCheckOutRequestAvro, sagaId, roomOutboxMessage, outboxCallback);

            // Log thành công
            logProcessingSuccess(roomOutboxMessage, sagaId);

        } catch (Exception e) {
            // Log và xử lý lỗi
            handleProcessingError(roomOutboxMessage, sagaId, e);
        }
    }

    /**
     * Validate các tham số đầu vào
     * <p>
     * Đảm bảo tất cả thông tin cần thiết cho việc gửi message đều hợp lệ
     */
    private void validateInputParameters(RoomOutboxMessage roomOutboxMessage,
                                         BiConsumer<RoomOutboxMessage, OutboxStatus> outboxCallback) {
        Assert.notNull(roomOutboxMessage, "RoomOutboxMessage không được null");
        Assert.notNull(outboxCallback, "OutboxCallback không được null");
        Assert.notNull(roomOutboxMessage.getPayload(), "RoomOutboxMessage payload không được null");
        Assert.notNull(roomOutboxMessage.getSagaId(), "RoomOutboxMessage sagaId không được null");
        Assert.hasText(roomOutboxMessage.getType(), "RoomOutboxMessage type không được empty");
    }

    /**
     * Trích xuất Saga ID từ outbox message
     * <p>
     * Saga ID được sử dụng để theo dõi quy trình checkout
     * và đảm bảo tính nhất quán trong distributed transaction
     */
    private String extractSagaId(RoomOutboxMessage roomOutboxMessage) {
        return roomOutboxMessage.getSagaId().toString();
    }

    /**
     * Log thông tin bắt đầu xử lý room check out request
     * <p>
     * Ghi log chi tiết về việc bắt đầu xử lý checkout phòng
     */
    private void logProcessingStart(RoomOutboxMessage roomOutboxMessage, String sagaId) {
        log.info("Bắt đầu xử lý RoomCheckOutRequest cho booking: {}, saga id: {}",
                roomOutboxMessage.getBookingId(),
                sagaId);
    }

    /**
     * Tạo BookingRoomRequestAvro model từ room outbox message
     * <p>
     * Chuyển đổi domain event thành Avro model để gửi qua Kafka
     */
    private BookingRoomRequestAvro createRoomCheckOutRequestAvro(String sagaId, ReservedEventPayload eventPayload) {
        return bookingDataMapper.bookingRoomCheckInEventToRoomRequestAvroModel(sagaId,eventPayload);
    }

    /**
     * Gửi message đến Kafka topic
     * <p>
     * Gửi thông tin checkout phòng đến room management service
     * để xử lý việc checkout thực tế
     */
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

    /**
     * Log thông tin xử lý thành công
     * <p>
     * Ghi log khi việc gửi room check out request thành công
     */
    private void logProcessingSuccess(RoomOutboxMessage roomOutboxMessage, String sagaId) {
        log.info("RoomCheckOutRequest đã được gửi thành công đến Kafka cho booking: {}, saga id: {}",
                roomOutboxMessage.getBookingId(),
                sagaId);
    }

    /**
     * Xử lý lỗi khi gửi message
     * <p>
     * Ghi log chi tiết lỗi và có thể thêm logic retry hoặc dead letter queue
     * để đảm bảo tính reliability của hệ thống
     */
    private void handleProcessingError(RoomOutboxMessage roomOutboxMessage,
                                       String sagaId,
                                       Exception exception) {
        log.error("Lỗi khi gửi RoomCheckOutRequest đến Kafka với booking: {}, saga id: {}. Lỗi: {}",
                roomOutboxMessage.getBookingId(),
                sagaId,
                exception.getMessage(),
                exception);

        // TODO: Có thể thêm logic retry hoặc dead letter queue ở đây
        // TODO: Có thể gửi notification đến admin về lỗi gửi message
        // TODO: Có thể implement circuit breaker pattern
    }
}
