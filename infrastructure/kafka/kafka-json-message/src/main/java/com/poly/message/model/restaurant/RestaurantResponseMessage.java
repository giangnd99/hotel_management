package com.poly.message.model.restaurant;

import com.poly.message.BaseResponse;
import com.poly.message.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class RestaurantResponseMessage extends BaseResponse {

    private String orderId;
    private String orderStatus;
    private Long processedAt;

    public RestaurantResponseMessage() { setMessageType(MessageType.RESPONSE); }
}
