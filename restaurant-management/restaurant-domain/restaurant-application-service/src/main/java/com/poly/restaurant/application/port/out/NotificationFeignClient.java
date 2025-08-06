package com.poly.restaurant.application.port.out;

import com.poly.restaurant.application.dto.NotificationRequestDTO;
import com.poly.restaurant.application.dto.NotificationResponseDTO;

public interface NotificationFeignClient {
    NotificationResponseDTO sendNotification(NotificationRequestDTO request);
    NotificationResponseDTO sendOrderStatusNotification(NotificationRequestDTO request);
}
