package com.poly.message.model.payment;

import com.poly.message.BaseResponse;
import com.poly.message.MessageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseMessage extends BaseResponse {

    private String paymentId;
    private String transactionRef;
    private String paymentStatus;
    private Long processedAt;

    public PaymentResponseMessage() {
        setMessageType(MessageType.RESPONSE);
    }
}
