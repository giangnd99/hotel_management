package com.poly.message.model.payment;

import com.poly.message.BaseMessage;
import com.poly.message.MessageType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
public class PaymentRequestMessage extends BaseMessage {

    private String paymentId;       // unique for payment attempt
    private String bookingId;
    private BigDecimal amount;
    private String currency;        // e.g. VND
    private String paymentMethod;   // CARD, CASH, ONLINE
    private Long paymentDate;       // client-sent time (optional)
    private String customerId;
    private String notes;

    public PaymentRequestMessage() {
        setMessageType(MessageType.REQUEST);
    }
}
