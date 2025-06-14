package edu.poly.notificationdataaccess.mapper;

import edu.poly.notificationdataaccess.entity.NotificationEntity;
import edu.poly.notificationdataaccess.entity.NotificationReceiverEntity;
import edu.poly.notificationdomaincore.entity.NotificationReceiver;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiverMapper {

    public NotificationReceiverEntity toEntity(NotificationReceiver domain, Integer notificationId) {
        NotificationReceiverEntity entity = new NotificationReceiverEntity();
        entity.setUserId(domain.getUserId());
        entity.setReceiverType(domain.getReceiverType());
        entity.setAdditionalEmail(domain.getAdditionalEmail());

        NotificationEntity notification = new NotificationEntity();
        notification.setNotificationId(notificationId);
        entity.setNotification(notification);

        return entity;
    }

    public NotificationReceiver toDomain(NotificationReceiverEntity entity) {
        return new NotificationReceiver(
                entity.getNotification().getNotificationId(),
                entity.getUserId(),
                entity.getReceiverType(),
                entity.getAdditionalEmail()
        );
    }
}