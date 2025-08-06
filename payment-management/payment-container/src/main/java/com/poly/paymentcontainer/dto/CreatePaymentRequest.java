package com.poly.paymentcontainer.dto;

import com.poly.paymentcontainer.share.ItemRequest;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
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
