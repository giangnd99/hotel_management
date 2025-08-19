package com.poly.booking.management.messaging.listener.kafka;

import com.poly.booking.management.domain.kafka.model.BookingRoomResponseAvro;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.port.in.message.listener.BookingCancellationListener;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.kafka.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Booking Cancellation Kafka Listener
 * <p>
 * CHỨC NĂNG:
 * - Lắng nghe messages từ Kafka topic về hủy booking
 * - Xử lý thông tin hủy booking từ room management service
 * - Trigger business logic hủy booking và xử lý hoàn tiền
 * <p>
 * MỤC ĐÍCH:
 * - Nhận thông tin hủy booking từ room service
 * - Thực hiện business logic hủy booking
 * - Xử lý hoàn tiền theo chính sách của khách sạn
 * <p>
 * BUSINESS RULES:
 * - Nhận tin nhắn từ room message response để trigger hủy booking
 * - Kiểm tra thời gian: nếu cách ngày check-in 1 ngày → cho phép hủy nhưng không hoàn tiền
 * - Hoàn tiền: Gửi tin nhắn đến payment service với topic hoàn tiền + số tiền đã cọc
 * <p>
 * KAFKA INTEGRATION:
 * - Lắng nghe từ topic "room-cancellation-topic"
 * - Xử lý Avro messages từ room service
 * - Chuyển đổi thành domain messages để xử lý business logic
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCancellationKafkaListener implements KafkaConsumer<BookingRoomResponseAvro> {

    private final BookingCancellationListener bookingCancellationListener;
    private final BookingMessageDataMapper bookingMessageDataMapper;

    /**
     * Xử lý messages từ Kafka topic
     * <p>
     * MESSAGE FLOW:
     * 1. Nhận Avro message từ Kafka topic
     * 2. Validate message format và content
     * 3. Chuyển đổi thành domain message
     * 4. Xử lý business logic hủy booking
     * 5. Xử lý hoàn tiền nếu đủ điều kiện
     * <p>
     * BUSINESS LOGIC:
     * - Kiểm tra điều kiện hủy booking
     * - Xác định có hoàn tiền hay không dựa trên thời gian
     * - Cập nhật trạng thái booking
     * - Gửi thông báo hoàn tiền đến payment service
     *
     * @param messages Danh sách Avro messages từ Kafka topic
     */
    @Override
    public void receive(List<BookingRoomResponseAvro> messages) {
        log.info("Received {} booking cancellation messages from Kafka", messages.size());

        messages.forEach(message -> {
            try {
                // Validate message
                validateCancellationMessage(message);

                // Convert Avro message to domain message
                RoomMessageResponse roomMessageResponse = convertToRoomMessageResponse(message);

                // Process cancellation based on message type
                processCancellationBusinessLogic(roomMessageResponse, message);

                log.info("Booking cancellation message processed successfully for booking: {}", 
                        message.getBookingId());

            } catch (Exception e) {
                log.error("Error processing booking cancellation message for booking: {}", 
                        message.getBookingId(), e);
                // TODO: Implement dead letter queue handling for failed messages
            }
        });
    }

    /**
     * Validate cancellation message
     * <p>
     * CHECKS:
     * - Message không được null
     * - Booking ID và Saga ID phải hợp lệ
     * - Reservation status phải hợp lệ
     *
     * @param message BookingRoomResponseAvro message cần validate
     * @throws IllegalArgumentException nếu validation fail
     */
    private void validateCancellationMessage(BookingRoomResponseAvro message) {
        if (message == null) {
            throw new IllegalArgumentException("Cancellation message cannot be null");
        }

        if (message.getBookingId() == null || message.getBookingId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        if (message.getSagaId() == null || message.getSagaId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Saga ID cannot be null or empty");
        }

        if (message.getReservationStatus() == null || message.getReservationStatus().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation status cannot be null or empty");
        }

        log.debug("Cancellation message validation passed for booking: {}", message.getBookingId());
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
                .reason(message.getReason())
                .build();
    }

    /**
     * Xử lý business logic dựa trên message type
     * <p>
     * Gọi business logic tương ứng với trạng thái cancellation
     *
     * @param roomMessageResponse RoomMessageResponse domain message
     * @param avroMessage        BookingRoomResponseAvro original message
     */
    private void processCancellationBusinessLogic(RoomMessageResponse roomMessageResponse, 
                                                BookingRoomResponseAvro avroMessage) {
        String reservationStatus = avroMessage.getReservationStatus();

        log.info("Processing cancellation business logic for booking: {} with status: {}", 
                avroMessage.getBookingId(), reservationStatus);

        switch (reservationStatus) {
            case "CANCELLED":
            case "FAILED":
                // Xử lý hủy booking
                bookingCancellationListener.processBookingCancellation(roomMessageResponse);
                break;
            case "ROLLBACK":
                // Xử lý rollback cancellation
                bookingCancellationListener.processBookingCancellationRollback(roomMessageResponse);
                break;
            default:
                log.warn("Unknown reservation status: {} for booking: {}", 
                        reservationStatus, avroMessage.getBookingId());
                break;
        }
    }
}
