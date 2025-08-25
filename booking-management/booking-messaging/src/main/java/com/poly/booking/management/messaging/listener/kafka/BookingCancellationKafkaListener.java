package com.poly.booking.management.messaging.listener.kafka;

import com.poly.booking.management.domain.kafka.model.BookingRoomResponseAvro;
import com.poly.booking.management.domain.kafka.model.NotificationMessageAvro;
import com.poly.booking.management.domain.message.reponse.NotificationMessageResponse;
import com.poly.booking.management.domain.message.reponse.RoomMessageResponse;
import com.poly.booking.management.domain.port.in.message.listener.BookingCancellationListener;
import com.poly.booking.management.messaging.mapper.BookingMessageDataMapper;
import com.poly.domain.valueobject.BookingStatus;
import com.poly.domain.valueobject.NotificationStatus;
import com.poly.kafka.consumer.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCancellationKafkaListener implements KafkaConsumer<NotificationMessageAvro> {

    private final BookingCancellationListener bookingCancellationListener;

    @Override
    @KafkaListener(topics = "booking-cancel-notification-topic",groupId = "booking-cancellation-group")
    public void receive(@Payload List<NotificationMessageAvro> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("Received {} booking cancellation messages from Kafka", messages.size());

        messages.forEach(message -> {
            try {
                validateCancellationMessage(message);

                NotificationMessageResponse roomMessageResponse = convertToRoomMessageResponse(message);

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

    private void validateCancellationMessage(NotificationMessageAvro message) {
        if (message == null) {
            throw new IllegalArgumentException("Cancellation message cannot be null");
        }

        if (message.getBookingId() == null || message.getBookingId().toString().trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }
        log.debug("Cancellation message validation passed for booking: {}", message.getBookingId());
    }


    private NotificationMessageResponse convertToRoomMessageResponse(NotificationMessageAvro message) {

        return NotificationMessageResponse.builder()
                .bookingId(message.getBookingId())
                .sagaId(message.getId())
                .bookingStatus(BookingStatus.CANCELLED)
                .notificationStatus(NotificationStatus.SUCCESS)
                .customerId(message.getCustomerId())
                .build();
    }


    private void processCancellationBusinessLogic(NotificationMessageResponse emailMessageResponse,
                                                  NotificationMessageAvro avroMessage) {
        String reservationStatus = avroMessage.getMessageStatus().name();

        log.info("Processing cancellation business logic for booking: {} with status: {}",
                avroMessage.getBookingId(), reservationStatus);

        if ("SUCCESS".equals(reservationStatus)) {
            bookingCancellationListener.processBookingCancellation(emailMessageResponse);
        }
    }


}
