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

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmedRequestKafkaPublisher implements NotificationRequestMessagePublisher {

    private final BookingMessageDataMapper bookingDataMapper;
    private final KafkaProducer<String, NotificationMessageAvro> kafkaProducer;
    private final BookingServiceConfigData bookingServiceConfigData;
    private final KafkaMessageHelper kafkaMessageHelper;


    @Override
    public void sendNotifi(NotifiOutboxMessage notifiOutboxMessage,
                           BiConsumer<NotifiOutboxMessage, OutboxStatus> outboxCallback) {

        validateInputParameters(notifiOutboxMessage, outboxCallback);

        NotifiEventPayload notificationEventPayload = extractNotificationEventPayload(notifiOutboxMessage);
        String sagaId = extractSagaId(notifiOutboxMessage);

        logProcessingStart(notificationEventPayload, sagaId);

        try {
            NotificationMessageAvro notificationModelAvro = createNotificationModelAvro(sagaId, notificationEventPayload);

            sendMessageToKafka(notificationModelAvro,
                    sagaId,
                    notifiOutboxMessage,
                    outboxCallback,
                    notificationEventPayload);

            logProcessingSuccess(notificationEventPayload, sagaId);

        } catch (Exception e) {
            handleProcessingError(notificationEventPayload, sagaId, e);
        }
    }

    @Override
    public void sendNotifiCancel(NotifiOutboxMessage notifiOutboxMessage, BiConsumer<NotifiOutboxMessage, OutboxStatus> outboxCallback) {
        validateInputParameters(notifiOutboxMessage, outboxCallback);
        NotifiEventPayload notificationEventPayload = extractNotificationEventPayload(notifiOutboxMessage);
        String sagaId = extractSagaId(notifiOutboxMessage);
        logProcessingStart(notificationEventPayload, sagaId);
        try {
            NotificationMessageAvro notificationModelAvro = createNotificationModelAvro(sagaId, notificationEventPayload);
            sendMessageToKafka(notificationModelAvro,
                    sagaId,
                    notifiOutboxMessage,
                    outboxCallback,
                    notificationEventPayload);
            logProcessingSuccess(notificationEventPayload, sagaId);
        } catch (Exception e) {
            handleProcessingError(notificationEventPayload, sagaId, e);
        }
    }

    private void validateInputParameters(NotifiOutboxMessage notifiOutboxMessage,
                                         BiConsumer<NotifiOutboxMessage, OutboxStatus> outboxCallback) {
        Assert.notNull(notifiOutboxMessage, "BookingNotifiOutboxMessage không được null");
        Assert.notNull(outboxCallback, "OutboxCallback không được null");
        Assert.notNull(notifiOutboxMessage.getPayload(), "BookingNotifiOutboxMessage payload không được null");
        Assert.notNull(notifiOutboxMessage.getSagaId(), "BookingNotifiOutboxMessage sagaId không được null");
        Assert.hasText(notifiOutboxMessage.getType(), "BookingNotifiOutboxMessage type không được empty");
    }


    private NotifiEventPayload extractNotificationEventPayload(NotifiOutboxMessage notifiOutboxMessage) {
        return kafkaMessageHelper.getEventPayload(
                notifiOutboxMessage.getPayload(),
                NotifiEventPayload.class
        );
    }

    private String extractSagaId(NotifiOutboxMessage notifiOutboxMessage) {
        return notifiOutboxMessage.getSagaId().toString();
    }

    private void logProcessingStart(NotifiEventPayload notificationEventPayload, String sagaId) {
        log.info("Bắt đầu xử lý BookingConfirmationNotification cho booking: {}, customer: {}, saga id: {}",
                notificationEventPayload.getBookingId(),
                notificationEventPayload.getCustomerId(),
                sagaId);
    }

    private NotificationMessageAvro createNotificationModelAvro(String sagaId,
                                                                NotifiEventPayload notificationEventPayload) {
        return bookingDataMapper.bookingCancelToNotificationModelAvro(sagaId, notificationEventPayload);
    }

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


    private void logProcessingSuccess(NotifiEventPayload notificationEventPayload, String sagaId) {
        log.info("BookingConfirmationNotification đã được gửi thành công đến Kafka cho booking: {}, customer: {}, saga id: {}",
                notificationEventPayload.getBookingId(),
                notificationEventPayload.getCustomerId(),
                sagaId);
    }

    private void handleProcessingError(NotifiEventPayload notificationEventPayload,
                                       String sagaId,
                                       Exception exception) {
        log.error("Lỗi khi gửi BookingConfirmationNotification đến Kafka với booking: {}, customer: {}, saga id: {}. Lỗi: {}",
                notificationEventPayload.getBookingId(),
                notificationEventPayload.getCustomerId(),
                sagaId,
                exception.getMessage(),
                exception);
    }
}
