package com.poly.message.model.notification;

import com.poly.message.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class NotificationResponseMessage extends BaseResponse {
    private String notificationId;
    private Long sentAt;

    public NotificationResponseMessage() {
        setMessageType(com.poly.message.MessageType.RESPONSE);
    }
}
