package com.poly.message.model.restaurant;

import com.poly.message.BaseMessage;
import com.poly.message.MessageType;

import java.math.BigDecimal;
import java.util.List;

public class RestaurantRequestMessage extends BaseMessage {
    private String orderId;
    private String customerId;
    private List<String> menuItems; // item ids
    private BigDecimal totalAmount;
    private String orderStatus;     // ORDERED, PREPARING, SERVED, CANCELLED

    public RestaurantRequestMessage() { setMessageType(MessageType.REQUEST); }
}
