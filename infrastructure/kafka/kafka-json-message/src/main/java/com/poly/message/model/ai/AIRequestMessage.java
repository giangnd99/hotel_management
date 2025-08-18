package com.poly.message.model.ai;

import com.poly.message.BaseMessage;
import com.poly.message.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class AIRequestMessage extends BaseMessage {
    private String requestId;
    private String customerId;
    private String question;
    private String aiModel;          // gpt-*, llama-*, ...
    private Long timeoutMs;          // optional

    public AIRequestMessage() { setMessageType(MessageType.REQUEST); }
}
