package com.poly.restaurant.client;

import com.poly.restaurant.application.dto.NotificationRequestDTO;
import com.poly.restaurant.application.dto.NotificationResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationFeignClientFallback implements NotificationFeignClient {

    @Override
    public NotificationResponseDTO sendNotification(NotificationRequestDTO request) {
        log.error("Notification service is unavailable. Fallback triggered for sendNotification with customerId={}", 
            request.customerId());
        return new NotificationResponseDTO(
                null, null, null,
                "FAILED", null,
                "Notification service is temporarily unavailable"
        );
    }

    @Override
    public NotificationResponseDTO sendOrderStatusNotification(NotificationRequestDTO request) {
        log.error("Notification service is unavailable. Fallback triggered for sendOrderStatusNotification with orderId={}", 
            request.orderId());
        return new NotificationResponseDTO(
                null, null, null,
                "FAILED", null,
                "Notification service is temporarily unavailable"
        );
    }
}