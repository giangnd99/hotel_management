package com.poly.message.model.restaurant;

import com.poly.message.BaseResponse;
import com.poly.message.MessageType;

public class RestaurantResponseMessage extends BaseResponse {

    private String orderId;
    private String orderStatus;
    private Long processedAt;

    public RestaurantResponseMessage() { setMessageType(MessageType.RESPONSE); }
}
