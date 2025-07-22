package edu.poly.notificationapplicationservice.exception;


import edu.poly.notificationdataaccess.entity.NotificationEntity;

import java.util.List;

public interface CustomNotificationRepository {
    List<NotificationEntity> findRecentNotifications(int limit);
}