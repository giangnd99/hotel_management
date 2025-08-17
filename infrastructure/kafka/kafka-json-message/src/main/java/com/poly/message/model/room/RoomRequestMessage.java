package com.poly.message.model.room;

import com.poly.message.BaseMessage;
import lombok.Builder;

@Builder
public class RoomRequestMessage extends BaseMessage {

    private String roomId;
    private String roomType;
    private String customerId;

    public RoomRequestMessage() {
        setMessageType(com.poly.message.MessageType.REQUEST);
    }
}
