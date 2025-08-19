package com.poly.booking.management.messaging.publisher.kafka;

import com.poly.booking.management.domain.config.BookingServiceConfigData;
import com.poly.booking.management.domain.kafka.model.NotificationMessageAvro;
import com.poly.booking.management.domain.outbox.payload.NotifiEventPayload;
import com.poly.booking.management.domain.outbox.model.NotifiOutboxMessage;
import com.poly.booking.management.domain.port.out.message.publisher.NotificationRequestMessagePublisher;
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
 * Kafka Publisher chịu trách nhiệm gửi thông báo xác nhận booking (booking confirmation notification)
 * đến Kafka topic để xử lý việc gửi thông báo cho khách hàng.
 * <p>
 * NGHIỆP VỤ:
 * - Gửi thông báo xác nhận booking sau khi thanh toán thành công
 * - Gửi QR code để khách hàng check-in tại khách sạn
 * - Đảm bảo tính nhất quán dữ liệu thông qua Outbox Pattern
 * - Xử lý bất đồng bộ cho việc gửi notification
 * <p>
 * PATTERNS ÁP DỤNG:
 * - Outbox Pattern: Đảm bảo message delivery reliability
 * - Saga Pattern: Quản lý distributed transaction cho booking confirmation process
 * - Domain Events: Tách biệt business logic notification
 * <p>
 * FLOW XỬ LÝ:
 * 1. Nhận booking confirmation event từ payment service
 * 2. Tạo notification payload với QR code và booking details
 * 3. Gửi notification đến notification service
 * 4. Cập nhật outbox status sau khi gửi thành công
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmedRequestKafkaPublisher implements NotificationRequestMessagePublisher {

    private final BookingMessageDataMapper bookingDataMapper;
    private final KafkaProducer<String, NotificationMessageAvro> kafkaProducer;
    private final BookingServiceConfigData bookingServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;

    /**
     * Gửi thông báo xác nhận booking với QR code đến Kafka topic
     * <p>
     * NGHIỆP VỤ:
     * - Xử lý thông báo xác nhận booking từ booking system
     * - Gửi QR code và thông tin booking đến notification service
     * - Đảm bảo tính nhất quán dữ liệu thông qua outbox pattern
     *
     * @param notifiOutboxMessage Message chứa thông tin notification từ outbox
     * @param outboxCallback      Callback function để cập nhật trạng thái outbox
     */
    @Override
    public void sendNotifi(NotifiOutboxMessage notifiOutboxMessage,
                           BiConsumer<NotifiOutboxMessage, OutboxStatus> outboxCallback) {

        // Validate input parameters
        validateInputParameters(notifiOutboxMessage, outboxCallback);

        // Extract và parse thông tin từ outbox message
        NotifiEventPayload notificationEventPayload = extractNotificationEventPayload(notifiOutboxMessage);
        String sagaId = extractSagaId(notifiOutboxMessage);

        // Log thông tin bắt đầu xử lý
        logProcessingStart(notificationEventPayload, sagaId);

        try {
            // Tạo Avro model từ notification event
            NotificationMessageAvro notificationModelAvro = createNotificationModelAvro(sagaId, notificationEventPayload);

            // Gửi message đến Kafka
            sendMessageToKafka(notificationModelAvro,
                    sagaId,
                    notifiOutboxMessage,
                    outboxCallback,
                    notificationEventPayload);

            // Log thành công
            logProcessingSuccess(notificationEventPayload, sagaId);

        } catch (Exception e) {
            // Log và xử lý lỗi
            handleProcessingError(notificationEventPayload, sagaId, e);
        }
    }

    /**
     * Validate các tham số đầu vào
     * <p>
     * Đảm bảo tất cả thông tin cần thiết cho việc gửi notification đều hợp lệ
     */
    private void validateInputParameters(NotifiOutboxMessage notifiOutboxMessage,
                                         BiConsumer<NotifiOutboxMessage, OutboxStatus> outboxCallback) {
        Assert.notNull(notifiOutboxMessage, "BookingNotifiOutboxMessage không được null");
        Assert.notNull(outboxCallback, "OutboxCallback không được null");
        Assert.notNull(notifiOutboxMessage.getPayload(), "BookingNotifiOutboxMessage payload không được null");
        Assert.notNull(notifiOutboxMessage.getSagaId(), "BookingNotifiOutboxMessage sagaId không được null");
        Assert.hasText(notifiOutboxMessage.getType(), "BookingNotifiOutboxMessage type không được empty");
    }

    /**
     * Trích xuất và parse BookingNotifiEventPayload từ outbox message
     * <p>
     * Chứa thông tin chi tiết về notification cần gửi:
     * - bookingId: ID của booking
     * - customerId: ID của khách hàng
     * - qrCode: QR code để check-in
     * - checkInTime: Thời gian check-in
     * - notificationStatus: Trạng thái notification
     */
    private NotifiEventPayload extractNotificationEventPayload(NotifiOutboxMessage notifiOutboxMessage) {
        return kafkaMessageHelper.getEventPayload(
                notifiOutboxMessage.getPayload(),
                NotifiEventPayload.class
        );
    }

    /**
     * Trích xuất Saga ID từ outbox message
     * <p>
     * Saga ID được sử dụng để theo dõi quy trình gửi notification
     * và đảm bảo tính nhất quán trong distributed transaction
     */
    private String extractSagaId(NotifiOutboxMessage notifiOutboxMessage) {
        return notifiOutboxMessage.getSagaId().toString();
    }

    /**
     * Log thông tin bắt đầu xử lý notification request
     * <p>
     * Ghi log chi tiết về việc bắt đầu xử lý gửi thông báo xác nhận
     */
    private void logProcessingStart(NotifiEventPayload notificationEventPayload, String sagaId) {
        log.info("Bắt đầu xử lý BookingConfirmationNotification cho booking: {}, customer: {}, saga id: {}",
                notificationEventPayload.getBookingId(),
                notificationEventPayload.getCustomerId(),
                sagaId);
    }

    /**
     * Tạo NotificationModelAvro model từ thông tin notification event
     * <p>
     * Chuyển đổi domain event thành Avro model để gửi qua Kafka
     */
    private NotificationMessageAvro createNotificationModelAvro(String sagaId,
                                                                NotifiEventPayload notificationEventPayload) {
        return bookingDataMapper.bookingNotificationEventToNotificationModelAvro(sagaId, notificationEventPayload);
    }

    /**
     * Gửi message đến Kafka topic
     * <p>
     * Gửi thông báo xác nhận booking đến notification service
     * để xử lý việc gửi email/SMS cho khách hàng
     */
    private void sendMessageToKafka(NotificationMessageAvro notificationModelAvro,
                                    String sagaId,
                                    NotifiOutboxMessage notifiOutboxMessage,
                                    BiConsumer<NotifiOutboxMessage, OutboxStatus> outboxCallback,
                                    NotifiEventPayload notificationEventPayload) {

        String topicName = bookingServiceConfigData.getBookingNotificationRequestTopicName();

        kafkaProducer.send(
                topicName,
                sagaId,
                notificationModelAvro,
                kafkaMessageHelper.getKafkaCallback(
                        topicName,
                        notificationModelAvro,
                        notifiOutboxMessage,
                        outboxCallback,
                        notificationEventPayload.getBookingId().toString(),
                        "BookingConfirmationNotificationAvroModel"
                )
        );
    }

    /**
     * Log thông tin xử lý thành công
     * <p>
     * Ghi log khi việc gửi thông báo xác nhận booking thành công
     */
    private void logProcessingSuccess(NotifiEventPayload notificationEventPayload, String sagaId) {
        log.info("BookingConfirmationNotification đã được gửi thành công đến Kafka cho booking: {}, customer: {}, saga id: {}",
                notificationEventPayload.getBookingId(),
                notificationEventPayload.getCustomerId(),
                sagaId);
    }

    /**
     * Xử lý lỗi khi gửi message
     * <p>
     * Ghi log chi tiết lỗi và có thể thêm logic retry hoặc dead letter queue
     * để đảm bảo tính reliability của hệ thống notification
     */
    private void handleProcessingError(NotifiEventPayload notificationEventPayload,
                                       String sagaId,
                                       Exception exception) {
        log.error("Lỗi khi gửi BookingConfirmationNotification đến Kafka với booking: {}, customer: {}, saga id: {}. Lỗi: {}",
                notificationEventPayload.getBookingId(),
                notificationEventPayload.getCustomerId(),
                sagaId,
                exception.getMessage(),
                exception);

        // TODO: Có thể thêm logic retry hoặc dead letter queue ở đây
        // TODO: Có thể gửi notification đến admin về lỗi gửi thông báo
        // TODO: Có thể gửi fallback notification (SMS thay vì email)
        // throw new NotificationPublishingException("Không thể gửi booking confirmation notification", exception);
    }
}
