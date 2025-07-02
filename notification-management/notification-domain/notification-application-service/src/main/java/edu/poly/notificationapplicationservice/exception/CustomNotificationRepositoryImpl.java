// CustomNotificationRepositoryImpl.java
package edu.poly.notificationapplicationservice.exception;


import edu.poly.notificationdataaccess.entity.NotificationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class CustomNotificationRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<NotificationEntity> findRecentNotifications(int limit) {
        return entityManager.createQuery(
                        "SELECT n FROM NotificationEntity n ORDER BY n.sentDate DESC", NotificationEntity.class)
                .setMaxResults(limit)
                .getResultList();
    }
}