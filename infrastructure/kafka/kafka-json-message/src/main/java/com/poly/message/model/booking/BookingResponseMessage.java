package com.poly.message.model.booking;

import com.poly.message.BaseResponse;
import com.poly.message.MessageType;

public class BookingResponseMessage extends BaseResponse {
    private String bookingId;
    private String bookingStatus;   // final/updated status
    private Long processedAt;

    public BookingResponseMessage() { setMessageType(MessageType.RESPONSE); }
}
