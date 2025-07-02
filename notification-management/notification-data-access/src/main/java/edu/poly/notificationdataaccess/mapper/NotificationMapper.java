package edu.poly.notificationdataaccess.mapper;

import edu.poly.notificationdataaccess.entity.NotificationEntity;
import edu.poly.notificationdomaincore.entity.Notification;
import edu.poly.notificationdomaincore.value_object.MessageContent;
import edu.poly.notificationdomaincore.value_object.NotificationMethod;
import edu.poly.notificationdomaincore.value_object.NotificationPriority;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationEntity toEntity(Notification domain) {
        NotificationEntity entity = new NotificationEntity();
        entity.setNotificationId(domain.getNotificationId());
        entity.setUserId(domain.getUserId());
        entity.setNotificationMethod(domain.getNotificationMethod().name());
        entity.setMessage(domain.getMessage().getContent());
        entity.setPriority(domain.getPriority().name());
        entity.setSentDate(domain.getSentDate());
        entity.setStatus(domain.getStatus().name());
        return entity;
    }

    public Notification toDomain(NotificationEntity entity) {
        return new Notification(
                entity.getUserId(),
                new MessageContent(entity.getMessage()),
                NotificationMethod.valueOf(entity.getNotificationMethod()),
                NotificationPriority.valueOf(entity.getPriority())
        );
    }
}