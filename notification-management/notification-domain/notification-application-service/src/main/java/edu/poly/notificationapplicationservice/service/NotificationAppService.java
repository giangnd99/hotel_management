package edu.poly.notificationapplicationservice.service;


import edu.poly.notificationapplicationservice.dto.NotificationRequest;
import edu.poly.notificationapplicationservice.dto.NotificationResponse;

import java.util.List;

public interface NotificationAppService {
    NotificationResponse sendNotification(NotificationRequest request);
    List<NotificationResponse> getUserNotifications(Integer userId);
    void markAsRead(Integer notificationId);
    void resendFailedNotifications();
}