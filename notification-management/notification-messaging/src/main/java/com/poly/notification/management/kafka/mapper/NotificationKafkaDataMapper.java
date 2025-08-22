package com.poly.notification.management.kafka.mapper;


import com.poly.booking.management.domain.kafka.model.MessageStatus;
import com.poly.booking.management.domain.kafka.model.NotificationMessageAvro;
import com.poly.booking.management.domain.kafka.model.NotificationType;
import com.poly.notification.management.message.NotificationMessage;
import org.springframework.stereotype.Component;

@Component
public class NotificationKafkaDataMapper {

    public NotificationMessageAvro toAvro(NotificationMessage message) {
        return NotificationMessageAvro.newBuilder()
                .setId(message.getId())
                .setMessageStatus(MessageStatus.valueOf(message.getMessageStatus().name()))
                .setCustomerEmail(message.getCustomerEmail())
                .setBookingId(message.getBookingId())
                .setCustomerId(message.getCustomerId())
                .setNotificationType(NotificationType.valueOf(message.getNotificationType().name()))
                .setQrCode(message.getQrCode())
                .build();
    }

    public NotificationMessage toEntity(NotificationMessageAvro notificationModelAvro) {
        return NotificationMessage.builder()
                .id(notificationModelAvro.getId())
                .bookingId(notificationModelAvro.getBookingId())
                .customerId(notificationModelAvro.getCustomerId())
                .customerEmail(notificationModelAvro.getCustomerEmail())
                .notificationType(com.poly.notification.management.message.NotificationType.valueOf(notificationModelAvro.getNotificationType().name()))
                .messageStatus(com.poly.notification.management.message.MessageStatus.valueOf(notificationModelAvro.getMessageStatus().name()))
                .build();
    }
}
