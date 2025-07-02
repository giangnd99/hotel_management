package edu.poly.notificationdomaincore.service;


import edu.poly.notificationdomaincore.entity.Notification;
import edu.poly.notificationdomaincore.entity.NotificationStatus;
import edu.poly.notificationdomaincore.exception.NotificationException;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationSenderService implements NotificationSenderService {

    @Override
    public void send(Notification notification) throws NotificationException {
        // Implement logic gửi email thực tế ở đây
        System.out.println("Sending email to user " + notification.getUserId());
        System.out.println("Content: " + notification.getMessage().getContent());
    }

    @Override
    public NotificationStatus getSupportedStatus() {
        return NotificationStatus.SENT;
    }
}