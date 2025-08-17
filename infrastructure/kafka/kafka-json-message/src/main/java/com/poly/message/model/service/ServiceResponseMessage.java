package com.poly.message.model.service;

import com.poly.message.BaseResponse;
import com.poly.message.MessageType;

public class ServiceResponseMessage extends BaseResponse {
    private String serviceRequestId;
    private String serviceStatus;
    private Long processedAt;

    public ServiceResponseMessage() { setMessageType(MessageType.RESPONSE); }
}
