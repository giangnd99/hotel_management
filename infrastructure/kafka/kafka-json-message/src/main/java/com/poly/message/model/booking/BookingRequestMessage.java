package com.poly.message.model.booking;

import com.poly.message.BaseMessage;
import com.poly.message.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@SuperBuilder
public class BookingRequestMessage extends BaseMessage {

    private String bookingId;       // unique booking code
    private String customerId;
    private List<String> roomIds;
    private Long checkInDate;
    private Long checkOutDate;
    private String bookingStatus;   // CREATED, CONFIRMED, CANCELLED, COMPLETED
    private BigDecimal totalPrice;
    private String notes;

    public BookingRequestMessage() { setMessageType(MessageType.REQUEST); }
}
