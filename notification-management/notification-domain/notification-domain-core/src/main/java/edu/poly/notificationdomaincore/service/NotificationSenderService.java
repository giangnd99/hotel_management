package edu.poly.notificationdomaincore.service;


import edu.poly.notificationdomaincore.entity.Notification;
import edu.poly.notificationdomaincore.exception.NotificationException;
import edu.poly.notificationdomaincore.value_object.NotificationStatus;

/**
 * Service trừu tượng cho việc gửi thông báo
 */
public interface NotificationSenderService {
    void send(Notification notification) throws NotificationException;
    NotificationStatus getSupportedStatus();
}