package com.poly.message.model.ai;

import com.poly.message.BaseResponse;
import com.poly.message.MessageType;

public class AIResponseMessage extends BaseResponse {

    private String requestId;
    private String answer;
    private Long responseTimeMs;

    public AIResponseMessage() { setMessageType(MessageType.RESPONSE); }
}
