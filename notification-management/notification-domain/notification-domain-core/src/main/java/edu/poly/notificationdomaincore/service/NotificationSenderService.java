package edu.poly.notificationdomaincore.service;


import edu.poly.notificationdomaincore.entity.Notification;
import edu.poly.notificationdomaincore.entity.NotificationStatus;
import edu.poly.notificationdomaincore.exception.NotificationException;

/**
 * Service trừu tượng cho việc gửi thông báo
 */
public interface NotificationSenderService {
    void send(Notification notification) throws NotificationException;
    NotificationStatus getSupportedStatus();
}