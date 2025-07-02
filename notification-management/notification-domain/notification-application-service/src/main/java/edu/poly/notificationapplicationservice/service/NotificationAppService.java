package edu.poly.notificationapplicationservice.service;


import edu.poly.notificationapplicationservice.dto.CreateNotificationRequest;
import edu.poly.notificationapplicationservice.dto.NotificationRequest;
import edu.poly.notificationapplicationservice.dto.NotificationResponse;
import edu.poly.notificationapplicationservice.exception.ApplicationNotificationException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationAppService {
    NotificationResponse sendNotification(NotificationRequest request);
    List<NotificationResponse> getUserNotifications(Integer userId);
    void markAsRead(Integer notificationId);
    void resendFailedNotifications();
    NotificationResponse createNotification(CreateNotificationRequest request) throws ApplicationNotificationException;
    Page<NotificationResponse> getUserNotifications(Integer userId, int page, int size);
}