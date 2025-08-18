package com.poly.notification.management.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {

    private String id;
    private String bookingId;
    private String customerId;
    private String customerEmail;
    private String qrCode;
    private NotificationType notificationType;
    private MessageStatus messageStatus;
}
