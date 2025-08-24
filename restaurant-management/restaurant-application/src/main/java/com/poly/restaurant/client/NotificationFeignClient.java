package com.poly.restaurant.client;

import com.poly.restaurant.application.dto.NotificationRequestDTO;
import com.poly.restaurant.application.dto.NotificationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "notification-service",
    url = "${notification.service.url:http://localhost:8091}",
    fallback = NotificationFeignClientFallback.class
)
public interface NotificationFeignClient {

    @PostMapping("/api/notifications/send")
    NotificationResponseDTO sendNotification(@RequestBody NotificationRequestDTO request);

    @PostMapping("/api/notifications/order-status")
    NotificationResponseDTO sendOrderStatusNotification(@RequestBody NotificationRequestDTO request);
}