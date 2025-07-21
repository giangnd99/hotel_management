package edu.poly.notificationdataaccess.repository;


import edu.poly.notificationdataaccess.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Integer> {

    @Query("SELECT n FROM NotificationEntity n WHERE n.userId = :userId ORDER BY n.sentDate DESC")
    List<NotificationEntity> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.status = :status")
    List<NotificationEntity> findByStatus(@Param("status") String status);
}