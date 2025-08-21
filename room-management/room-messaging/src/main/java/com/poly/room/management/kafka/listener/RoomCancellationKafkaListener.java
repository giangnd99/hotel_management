package com.poly.room.management.kafka.listener;

import com.poly.booking.management.domain.kafka.model.BookingRoomResponseAvro;
import com.poly.room.management.domain.message.RoomCancellationResponseMessage;
import com.poly.room.management.domain.port.in.message.listener.RoomCancellationListener;
import com.poly.room.management.kafka.mapper.RoomKafkaDataMapper;
import com.poly.kafka.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Room Cancellation Kafka Listener
 * <p>
 * CHỨC NĂNG:
 * - Lắng nghe messages từ Kafka topic về hủy phòng
 * - Xử lý thông tin hủy phòng từ booking management service
 * - Trigger business logic hủy phòng và giải phóng phòng
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin hủy phòng từ booking service
 * - Thực hiện business logic hủy phòng
 * - Xử lý giải phóng phòng và cập nhật trạng thái
 * <p>
 * BUSINESS RULES:
 * - Nhận tin nhắn từ booking service để trigger hủy phòng
 * - Giải phóng phòng và cập nhật trạng thái về VACANT
 * - Gửi thông báo hủy phòng đến booking service
 * <p>
 * KAFKA INTEGRATION:
 * - Lắng nghe từ topic "room-cancellation-topic"
 * - Xử lý Avro messages từ booking service
 * - Chuyển đổi thành domain messages để xử lý business logic
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RoomCancellationKafkaListener implements KafkaConsumer<BookingRoomResponseAvro> {

    private final RoomCancellationListener roomCancellationListener;
    private final RoomKafkaDataMapper roomKafkaDataMapper;

    @Override
    @KafkaListener(topics = "room-cancellation-response", groupId = "room-cancellation-response")
    public void receive(@Payload List<BookingRoomResponseAvro> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("Received {} room cancellation messages from Kafka", messages.size());

        messages.forEach(message -> {
            try {
                // Validate message
                validateCancellationMessage(message);

                RoomCancellationResponseMessage bookingRoomRequestMessage = convertToBookingRoomRequestMessage(message);

                processCancellationBusinessLogic(bookingRoomRequestMessage, message);

                log.info("Room cancellation message processed successfully for saga: {}",
                        message.getSagaId());

            } catch (Exception e) {
                log.error("Error processing room cancellation message for saga: {}",
                        message.getSagaId(), e);
                // TODO: Implement dead letter queue handling for failed messages
            }
        });
    }

    /**
     * Validate cancellation message
     * <p>
     * CHECKS:
     * - Message không được null
     * - Saga ID và Booking ID phải hợp lệ
     * - Booking status phải hợp lệ
     *
     * @param message BookingRoomRequestAvro message cần validate
     * @throws IllegalArgumentException nếu validation fail
     */
    private void validateCancellationMessage(BookingRoomResponseAvro message) {
        if (message == null) {
            throw new IllegalArgumentException("Cancellation message cannot be null");
        }

        if (message.getSagaId() == null || message.getSagaId().trim().isEmpty()) {
            throw new IllegalArgumentException("Saga ID cannot be null or empty");
        }

        if (message.getBookingId() == null || message.getBookingId().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        if (message.getRooms() == null || message.getReservationStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking status cannot be null or empty");
        }

        log.debug("Cancellation message validation passed for saga: {}", message.getSagaId());
    }

    /**
     * Chuyển đổi BookingRoomRequestAvro thành BookingRoomRequestMessage
     * <p>
     * Sử dụng mapper để chuyển đổi giữa Avro model và domain message
     *
     * @param message BookingRoomRequestAvro message
     * @return BookingRoomRequestMessage domain message
     */
    private RoomCancellationResponseMessage convertToBookingRoomRequestMessage(BookingRoomResponseAvro message) {
        return roomKafkaDataMapper.toRoomCancellationResponseMessage(message);
    }

    /**
     * Xử lý business logic dựa trên message type
     * <p>
     * Gọi business logic tương ứng với trạng thái cancellation
     *
     * @param bookingRoomRequestMessage BookingRoomRequestMessage domain message
     * @param avroMessage               BookingRoomRequestAvro original message
     */
    private void processCancellationBusinessLogic(RoomCancellationResponseMessage bookingRoomRequestMessage,
                                                  BookingRoomResponseAvro avroMessage) {
        String bookingStatus = avroMessage.getReservationStatus();

        log.info("Processing cancellation business logic for saga: {} with status: {}",
                avroMessage.getSagaId(), bookingStatus);

        roomCancellationListener.processRoomCancellation(bookingRoomRequestMessage);

    }
}
