package edu.poly.notificationdataaccess.repository;


import edu.poly.notificationdataaccess.entity.NotificationReceiverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationReceiverJpaRepository extends JpaRepository<NotificationReceiverEntity, Integer> {
    List<NotificationReceiverEntity> findByNotificationId(Integer notificationId);
}