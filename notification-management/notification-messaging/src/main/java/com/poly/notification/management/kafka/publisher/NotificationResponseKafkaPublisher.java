package com.poly.notification.management.kafka.publisher;

import com.poly.booking.management.domain.kafka.model.NotificationMessageAvro;
import com.poly.notification.management.kafka.mapper.NotificationKafkaDataMapper;
import com.poly.kafka.producer.AbstractKafkaPublisher;
import com.poly.kafka.producer.service.KafkaProducer;
import com.poly.notification.management.message.NotificationMessage;
import com.poly.notification.management.port.out.publisher.BookingConfirmedEmailResponsePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class NotificationResponseKafkaPublisher extends
        AbstractKafkaPublisher<String, NotificationMessageAvro, NotificationMessage>
        implements BookingConfirmedEmailResponsePublisher {
    private final String TOPIC_NAME = "notification-send-mail-response";
    private final String MESSAGE_NAME = "NotificationResponse";
    private final NotificationKafkaDataMapper notificationKafkaDataMapper;

    protected NotificationResponseKafkaPublisher(KafkaProducer<String, NotificationMessageAvro> kafkaProducer, NotificationKafkaDataMapper notificationKafkaDataMapper) {
        super(kafkaProducer);
        this.notificationKafkaDataMapper = notificationKafkaDataMapper;
    }

    @Override
    public void publish(NotificationMessage message) {
        super.publish(message);
    }

    @Override
    protected String getTopicName() {
        return TOPIC_NAME;
    }

    @Override
    protected String getKey(NotificationMessage message) {
        return message.getId();
    }

    @Override
    protected NotificationMessageAvro toAvro(NotificationMessage message) {
        return notificationKafkaDataMapper.toAvro(message);
    }

    @Override
    protected String getMessageName() {
        return MESSAGE_NAME;
    }
}
