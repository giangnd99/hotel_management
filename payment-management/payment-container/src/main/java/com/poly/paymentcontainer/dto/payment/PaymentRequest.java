package com.poly.paymentcontainer.dto.payment;

import com.poly.paymentcontainer.share.ItemRequest;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class PaymentRequest {
    private BigDecimal amount;
    private List<ItemRequest> items;
    private String note;
    private String paymentMethod;
    private String serviceType;
}
