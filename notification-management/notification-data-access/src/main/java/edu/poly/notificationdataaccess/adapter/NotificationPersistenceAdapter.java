package edu.poly.notificationdataaccess.adapter;

import edu.poly.notificationdataaccess.entity.NotificationEntity;
import edu.poly.notificationdataaccess.entity.NotificationReceiverEntity;
import edu.poly.notificationdataaccess.mapper.NotificationMapper;
import edu.poly.notificationdataaccess.mapper.NotificationReceiverMapper;
import edu.poly.notificationdataaccess.repository.NotificationJpaRepository;
import edu.poly.notificationdataaccess.repository.NotificationReceiverJpaRepository;
import edu.poly.notificationdomaincore.entity.Notification;
import edu.poly.notificationdomaincore.service.NotificationPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationPersistenceAdapter implements NotificationPersistencePort {

    private final NotificationJpaRepository notificationRepository;
    private final NotificationReceiverJpaRepository receiverRepository;
    private final NotificationMapper notificationMapper;
    private final NotificationReceiverMapper receiverMapper;

    @Override
    public Notification save(Notification notification) {
        // Lưu thông báo chính
        NotificationEntity entity = notificationMapper.toEntity(notification);
        NotificationEntity savedEntity = notificationRepository.save(entity);

        // Lưu danh sách người nhận
        if (notification.getReceivers() != null) {
            List<NotificationReceiverEntity> receiverEntities = notification.getReceivers().stream()
                    .map(receiver -> receiverMapper.toEntity(receiver, savedEntity.getNotificationId()))
                    .collect(Collectors.toList());

            receiverRepository.saveAll(receiverEntities);
        }

        return notificationMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Notification> findById(Integer notificationId) {
        return notificationRepository.findById(notificationId)
                .map(notificationMapper::toDomain);
    }

    @Override
    public List<Notification> findByUserId(Integer userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(notificationMapper::toDomain)
                .collect(Collectors.toList());
    }
}