package com.poly.message.model.notification;

import com.poly.message.BaseMessage;
import com.poly.message.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
public class NotificationRequestMessage extends BaseMessage {

    private String notificationId;
    private String customerId;
    private String title;
    private String content;
    private String notificationType; // EMAIL, SMS, APP_PUSH

    public NotificationRequestMessage() {
        setMessageType(MessageType.REQUEST);
    }
}
