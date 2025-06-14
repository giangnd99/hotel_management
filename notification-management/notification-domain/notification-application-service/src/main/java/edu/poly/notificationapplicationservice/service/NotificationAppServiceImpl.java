package edu.poly.notificationapplicationservice.service;



import edu.poly.notificationdomaincore.entity.Notification;
import edu.poly.notificationapplicationservice.dto.NotificationRequest;
import edu.poly.notificationapplicationservice.dto.NotificationResponse;
import edu.poly.notificationdomaincore.entity.NotificationReceiver;
import edu.poly.notificationdomaincore.entity.NotificationStatus;
import edu.poly.notificationdomaincore.exception.NotificationException;
import edu.poly.notificationdomaincore.service.NotificationPersistencePort;
import edu.poly.notificationdomaincore.service.NotificationSenderService;
import edu.poly.notificationdomaincore.service.NotificationValidationService;
import edu.poly.notificationdomaincore.value_object.MessageContent;
import edu.poly.notificationdomaincore.value_object.NotificationMethod;
import edu.poly.notificationdomaincore.value_object.NotificationPriority;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationAppServiceImpl implements NotificationAppService {

    private final NotificationPersistencePort persistencePort;
    private final NotificationValidationService validationService;
    private final NotificationSenderService senderService;

    @Override
    @Transactional
    public NotificationResponse sendNotification(NotificationRequest request) {
        try {
            // Chuyển đổi từ DTO sang Domain Object
            Notification notification = new Notification(
                    request.getUserId(),
                    new MessageContent(request.getMessage()),
                    NotificationMethod.valueOf(request.getMethod()),
                    NotificationPriority.valueOf(request.getPriority())
            );

            // Thêm người nhận nếu có
            if (request.getReceivers() != null) {
                List<NotificationReceiver> receivers = request.getReceivers().stream()
                        .map(r -> new NotificationReceiver(
                                null, // notificationId sẽ được set sau
                                r.getUserId(),
                                r.getReceiverType(),
                                r.getAdditionalEmail()
                        ))
                        .collect(Collectors.toList());

                notification.setReceivers(receivers);
            }

            // Validate thông báo
            validationService.validate(notification);

            // Gửi thông báo
            senderService.send(notification);
            notification.setStatus(NotificationStatus.SENT);

            // Lưu vào database
            Notification savedNotification = persistencePort.save(notification);

            // Chuyển đổi thành response
            return toResponse(savedNotification);

        } catch (NotificationException e) {
            throw new NotificationException("Failed to send notification: " + e.getMessage(), e);
        }
    }

    @Override
    public List<NotificationResponse> getUserNotifications(Integer userId) {
        return persistencePort.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Integer notificationId) {
        persistencePort.findById(notificationId).ifPresent(notification -> {
            notification.setStatus(NotificationStatus.READ);
            persistencePort.save(notification);
        });
    }

    @Override
    @Transactional
    public void resendFailedNotifications() {
        // Logic để gửi lại các thông báo thất bại
        // Có thể triển khai theo batch job
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setNotificationId(notification.getNotificationId());
        response.setMessage(notification.getMessage().getContent());
        response.setSentDate(notification.getSentDate());
        response.setStatus(notification.getStatus().name());
        return response;
    }
}