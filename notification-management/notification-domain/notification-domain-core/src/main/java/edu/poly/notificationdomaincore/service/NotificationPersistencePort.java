package edu.poly.notificationdomaincore.service;


import edu.poly.notificationdomaincore.entity.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationPersistencePort {
    Notification save(Notification notification);
    Optional<Notification> findById(Integer notificationId);
    List<Notification> findByUserId(Integer userId);
}