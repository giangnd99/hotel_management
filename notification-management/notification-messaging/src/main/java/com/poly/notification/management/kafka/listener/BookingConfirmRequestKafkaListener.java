package com.poly.notification.management.kafka.listener;

import com.poly.booking.management.domain.kafka.model.NotificationMessageAvro;
import com.poly.kafka.consumer.KafkaConsumer;
import com.poly.notification.management.kafka.mapper.NotificationKafkaDataMapper;
import com.poly.notification.management.port.in.listener.BookingConfirmEmailRequestListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookingConfirmRequestKafkaListener implements KafkaConsumer<NotificationMessageAvro> {

    private final BookingConfirmEmailRequestListener bookingConfirmEmailRequestListener;
    private final NotificationKafkaDataMapper notificationKafkaDataMapper;
    private final String TOPIC_NAME = "notification-send-mail-request";

    @Override
    @KafkaListener(topics = TOPIC_NAME, groupId = "notification-send-mail-request")
    public void receive(
            @Payload List<NotificationMessageAvro> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("Received message: {}", messages);
        messages.forEach(message -> {
            log.info("Message for booking id: {}", message.getBookingId());
            bookingConfirmEmailRequestListener.onBookingConfirmEmailRequest(notificationKafkaDataMapper.toEntity(message));
        });

    }
}
