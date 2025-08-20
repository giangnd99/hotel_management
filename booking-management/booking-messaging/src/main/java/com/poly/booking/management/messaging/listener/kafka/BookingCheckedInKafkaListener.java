package com.poly.booking.management.messaging.listener.kafka;

import com.poly.booking.management.domain.kafka.model.NotificationMessageAvro;
import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.booking.management.domain.port.in.message.listener.BookingCheckInListener;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.kafka.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingCheckedInKafkaListener implements KafkaConsumer<NotificationMessageAvro> {

    private final BookingCheckInListener bookingCheckInListener;
    private final BookingMessageDataMapper bookingMessageDataMapper;

    /**
     * Xử lý messages từ Kafka topic để thực hiện check-in
     * <p>
     * MESSAGE FLOW:
     * 1. Nhận Avro message từ Kafka topic
     * 2. Validate message format và content
     * 3. Chuyển đổi thành domain message
     * 4. Xử lý business logic check-in
     * 5. Cập nhật trạng thái booking
     * <p>
     * BUSINESS LOGIC:
     * - Kiểm tra điều kiện check-in
     * - Xác định phòng và khách hàng
     * - Cập nhật trạng thái booking thành CHECKED_IN
     * - Gửi thông báo xác nhận check-in
     *
     * @param messages Danh sách Avro messages từ Kafka topic
     */
    @Override
    @KafkaListener(topics = "room-check-in-notification-topic", groupId = "booking-check-in-group")
    public void receive(@Payload List<NotificationMessageAvro> messages,
                        @Header(KafkaHeaders.KEY) List<String> keys,
                        @Header(KafkaHeaders.PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("Received {} check-in notification messages from Kafka", messages.size());

        messages.forEach(message -> {
            try {
                // Validate message
                validateCheckInMessage(message);

                // Convert Avro message to domain message
                NotificationMessageResponse notificationMessageResponse = convertToNotificationMessageResponse(message);

                // Process check-in based on message type
                processCheckInBusinessLogic(notificationMessageResponse, message);

                log.info("Check-in notification message processed successfully for booking: {}",
                        message.getBookingId());

            } catch (Exception e) {
                log.error("Error processing check-in notification message for booking: {}",
                        message.getBookingId(), e);
                // TODO: Implement dead letter queue handling for failed messages
            }
        });
    }

    /**
     * Validate check-in message
     * <p>
     * CHECKS:
     * - Message không được null
     * - Booking ID và Customer ID phải hợp lệ
     * - Notification type phải là CHECK_IN
     * - Message status phải hợp lệ
     *
     * @param message NotificationMessageAvro message cần validate
     * @throws IllegalArgumentException nếu validation fail
     */
    private void validateCheckInMessage(NotificationMessageAvro message) {
        if (message == null) {
            throw new IllegalArgumentException("Check-in message cannot be null");
        }

        if (message.getBookingId() == null || message.getBookingId().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }

        if (message.getCustomerId() == null || message.getCustomerId().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }

        if (message.getNotificationType() == null) {
            throw new IllegalArgumentException("Notification type cannot be null");
        }

        if (message.getMessageStatus() == null) {
            throw new IllegalArgumentException("Message status cannot be null");
        }

        log.debug("Check-in message validation passed for booking: {}", message.getBookingId());
    }

    /**
     * Chuyển đổi NotificationMessageAvro thành NotificationMessageResponse
     * <p>
     * Sử dụng mapper để chuyển đổi giữa Avro model và domain message
     *
     * @param message NotificationMessageAvro message
     * @return NotificationMessageResponse domain message
     */
    private NotificationMessageResponse convertToNotificationMessageResponse(NotificationMessageAvro message) {
        // TODO: Implement mapping logic in BookingMessageDataMapper
        // return bookingMessageDataMapper.notificationMessageAvroToNotificationMessageResponse(message);

        // Temporary implementation - cần implement mapper method
        return NotificationMessageResponse.builder()
                .id(message.getId())
                .bookingId(message.getBookingId())
                .customerId(message.getCustomerId())
                .qrCode(message.getQrCode())
                .checkInTime(java.time.Instant.now()) // Sử dụng thời gian hiện tại
                .notificationStatus(com.poly.domain.valueobject.NotificationStatus.valueOf(message.getMessageStatus().name()))
                .bookingStatus(com.poly.domain.valueobject.BookingStatus.CHECKED_IN) // Mặc định là CHECKED_IN
                .failureMessages(java.util.List.of()) // Không có lỗi
                .build();
    }

    /**
     * Xử lý business logic dựa trên message type và status
     * <p>
     * Gọi business logic tương ứng với trạng thái check-in
     *
     * @param notificationMessageResponse NotificationMessageResponse domain message
     * @param avroMessage                 NotificationMessageAvro original message
     */
    private void processCheckInBusinessLogic(NotificationMessageResponse notificationMessageResponse,
                                            NotificationMessageAvro avroMessage) {
        String notificationType = avroMessage.getNotificationType().name();
        String messageStatus = avroMessage.getMessageStatus().name();

        log.info("Processing check-in business logic for booking: {} with type: {} and status: {}",
                avroMessage.getBookingId(), notificationType, messageStatus);

        // Chỉ xử lý nếu là notification type CHECK_IN
        if ("CHECK_IN".equals(notificationType)) {
            switch (messageStatus) {
                case "SUCCESS":
                    // Xử lý check-in thành công
                    bookingCheckInListener.processBookingCheckIn(notificationMessageResponse);
                    break;
                case "FAILED":
                    // Xử lý check-in thất bại
                    bookingCheckInListener.processBookingCheckInFailed(notificationMessageResponse);
                    break;
                case "PENDING":
                    // Xử lý check-in đang chờ
                    bookingCheckInListener.processBookingCheckInPending(notificationMessageResponse);
                    break;
                default:
                    log.warn("Unknown message status: {} for check-in notification of booking: {}",
                            messageStatus, avroMessage.getBookingId());
                    break;
            }
        } else {
            log.debug("Skipping non-check-in notification type: {} for booking: {}",
                    notificationType, avroMessage.getBookingId());
        }
    }
}