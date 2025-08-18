package com.poly.servicemanagement.messaging.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderResponseMessage {
    private String orderId;
    private String orderNumber;
    private String status;
    private String message;
    private LocalDateTime processedAt;
}
