package com.poly.message.model.customer;

import com.poly.message.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class CustomerResponseMessage extends BaseResponse {

    private String customerId;
    private String action;
    private Long processedAt;

    public CustomerResponseMessage() {
        setMessageType(com.poly.message.MessageType.RESPONSE);
    }
}
