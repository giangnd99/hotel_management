package com.poly.paymentcontainer.dto2;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreatePaymentRequest {
    private UUID bookingId;
    private UUID invoiceId;
    private UUID staffId;
//    private UUID voucherId;
//    private BigDecimal amountVoucher;
//    private List<ItemRequest> items;
//    private String note;
}
