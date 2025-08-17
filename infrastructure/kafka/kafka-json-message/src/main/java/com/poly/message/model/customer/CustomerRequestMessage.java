package com.poly.message.model.customer;

import com.poly.message.BaseMessage;

public class CustomerRequestMessage extends BaseMessage {

    private String customerId; // identity
    private String action;     // CREATE, UPDATE, QUERY
    private String fullName;
    private String email;
    private String phoneNumber;
    private String nationality;
    private String loyaltyLevel;

    public CustomerRequestMessage() {
        setMessageType(com.poly.message.MessageType.REQUEST);
    }
}
