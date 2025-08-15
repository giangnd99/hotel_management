package com.poly.message.model.service;

import com.poly.message.BaseMessage;
import com.poly.message.MessageType;

import java.math.BigDecimal;

public class ServiceRequestMessage extends BaseMessage {

    private String serviceRequestId;
    private String customerId;
    private String serviceType;     // SPA, LAUNDRY, TOUR, ...
    private String serviceStatus;   // REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED
    private BigDecimal servicePrice;

    public ServiceRequestMessage() { setMessageType(MessageType.REQUEST); }
}
