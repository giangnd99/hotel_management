package com.poly.message.model.notification;

import com.poly.message.BaseResponse;

public class NotificationResponseMessage extends BaseResponse {
    private String notificationId;
    private Long sentAt;

    public NotificationResponseMessage() {
        setMessageType(com.poly.message.MessageType.RESPONSE);
    }
}
