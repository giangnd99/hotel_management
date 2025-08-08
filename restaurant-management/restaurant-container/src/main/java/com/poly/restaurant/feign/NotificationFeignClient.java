package com.poly.restaurant.feign;

import com.poly.restaurant.dto.NotificationRequestDTO;
import com.poly.restaurant.dto.NotificationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "notification-service", url = "${notification.service.url}")
public interface NotificationFeignClient {

    @PostMapping("/api/notifications/send")
    NotificationResponseDTO sendNotification(@RequestBody NotificationRequestDTO request);

    @PostMapping("/api/notifications/order-status")
    NotificationResponseDTO sendOrderStatusNotification(@RequestBody NotificationRequestDTO request);
}
