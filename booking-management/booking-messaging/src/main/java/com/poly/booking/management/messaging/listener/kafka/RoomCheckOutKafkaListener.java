package com.poly.booking.management.messaging.listener.kafka;

import com.poly.booking.management.domain.kafka.model.BookingRoomResponseAvro;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.port.in.message.listener.RoomCheckOutListener;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.domain.valueobject.RoomStatus;
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
 * Room Check Out Kafka Listener
 * <p>
 * CHỨC NĂNG:
 * - Lắng nghe messages từ topic "room-check-out-topic"
 * - Xử lý thông tin checkout phòng từ room management service
 * - Chuyển đổi Avro model thành domain message và gọi business logic
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin checkout phòng từ Kafka topic
 * - Thực hiện checkout cho booking trong hệ thống
 * - Đảm bảo tính nhất quán dữ liệu thông qua Saga pattern
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Kafka Consumer Pattern: Lắng nghe messages từ topic
 * - Adapter Pattern: Chuyển đổi giữa Avro model và domain message
 * - Saga Pattern: Quản lý distributed transaction
 * <p>
 * FLOW XỬ LÝ:
 * 1. Nhận messages từ Kafka topic "room-check-out-topic"
 * 2. Chuyển đổi BookingRoomResponseAvro thành RoomMessageResponse
 * 3. Gọi business logic thông qua RoomCheckOutListener
 * 4. Xử lý checkout hoặc rollback dựa trên message type
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoomCheckOutKafkaListener implements KafkaConsumer<BookingRoomResponseAvro> {

    private final RoomCheckOutListener roomCheckOutListener;
    private final BookingMessageDataMapper bookingMessageDataMapper;

    /**
     * Xử lý messages nhận được từ Kafka topic
     * <p>
     * LOGIC FLOW:
     * 1. Validate input messages
     * 2. Chuyển đổi Avro model thành domain message
     * 3. Gọi business logic tương ứng với message type
     * 4. Log kết quả xử lý
     * <p>
     * ERROR HANDLING:
     * - Validate message data trước khi xử lý
     * - Log chi tiết lỗi nếu có exception
     * - Đảm bảo không làm crash listener khi có lỗi
     *
     * @param messages   Danh sách messages từ Kafka
     * @param keys       Danh sách keys tương ứng
     * @param partitions Danh sách partition numbers
     * @param offsets    Danh sách offset values
     */
    @Override
    @KafkaListener(topics = "room-check-out-request",groupId = "room-check-out-group")
    public void receive(
            @Payload List<BookingRoomResponseAvro> messages,
            @Header(KafkaHeaders.KEY) List<String> keys,
            @Header(KafkaHeaders.PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("Received {} room check out messages from Kafka topic", messages.size());

        try {
            // Xử lý từng message
            for (int i = 0; i < messages.size(); i++) {
                BookingRoomResponseAvro message = messages.get(i);
                String key = keys.get(i);
                Integer partition = partitions.get(i);
                Long offset = offsets.get(i);

                processRoomCheckOutMessage(message, key, partition, offset);
            }

            log.info("Successfully processed {} room check out messages", messages.size());

        } catch (Exception e) {
            log.error("Error processing room check out messages", e);
            // Không throw exception để tránh làm crash listener
        }
    }

    /**
     * Xử lý một room check out message
     * <p>
     * LOGIC:
     * 1. Validate message data
     * 2. Chuyển đổi Avro model thành domain message
     * 3. Gọi business logic dựa trên message type
     * 4. Log kết quả xử lý
     *
     * @param message   BookingRoomResponseAvro message
     * @param key       Message key
     * @param partition Partition number
     * @param offset    Offset value
     */
    private void processRoomCheckOutMessage(BookingRoomResponseAvro message, String key, Integer partition, Long offset) {
        try {
            log.debug("Processing room check out message - Key: {}, Partition: {}, Offset: {}", key, partition, offset);

            // Validate message
            validateRoomCheckOutMessage(message);

            // Chuyển đổi Avro model thành domain message
            RoomMessageResponse roomMessageResponse = convertToRoomMessageResponse(message);

            // Xử lý business logic dựa trên message type
            processRoomCheckOutBusinessLogic(roomMessageResponse);

            log.debug("Room check out message processed successfully - Key: {}, Partition: {}, Offset: {}",
                    key, partition, offset);

        } catch (Exception e) {
            log.error("Error processing room check out message - Key: {}, Partition: {}, Offset: {}. Error: {}",
                    key, partition, offset, e.getMessage(), e);
        }
    }

    /**
     * Validate room check out message
     * <p>
     * Đảm bảo message có đầy đủ thông tin cần thiết để xử lý
     *
     * @param message BookingRoomResponseAvro message cần validate
     * @throws IllegalArgumentException nếu message không hợp lệ
     */
    private void validateRoomCheckOutMessage(BookingRoomResponseAvro message) {
        if (message == null) {
            throw new IllegalArgumentException("Room check out message cannot be null");
        }

        if (message.getBookingId() == null || message.getBookingId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        if (message.getSagaId() == null || message.getSagaId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Saga ID cannot be null or empty");
        }

        log.debug("Room check out message validation passed for booking: {}", message.getBookingId());
    }

    /**
     * Chuyển đổi BookingRoomResponseAvro thành RoomMessageResponse
     * <p>
     * Sử dụng mapper để chuyển đổi giữa Avro model và domain message
     *
     * @param message BookingRoomResponseAvro message
     * @return RoomMessageResponse domain message
     */
    private RoomMessageResponse convertToRoomMessageResponse(BookingRoomResponseAvro message) {
        // TODO: Implement mapping logic in BookingMessageDataMapper
        // return bookingMessageDataMapper.bookingRoomResponseAvroToRoomMessageResponse(message);

        // Temporary implementation - cần implement mapper method
        return RoomMessageResponse.builder()
                .bookingId(message.getBookingId())
                .sagaId(message.getSagaId())
                .roomResponseStatus(RoomStatus.CHECKED_OUT)
                .build();
    }

    /**
     * Xử lý business logic dựa trên message type
     * <p>
     * Gọi business logic tương ứng với trạng thái checkout
     *
     * @param roomMessageResponse RoomMessageResponse domain message
     */
    private void processRoomCheckOutBusinessLogic(RoomMessageResponse roomMessageResponse) {


        // Xử lý checkout thành công
        roomCheckOutListener.roomCheckOutCompleted(roomMessageResponse);
        log.info("Room check out completed for booking: {}", roomMessageResponse.getBookingId());
    }
}
