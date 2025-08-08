package com.poly.paymentapplicationservice.dto;

import com.poly.paymentdomain.model.entity.InvoiceItem;
import com.poly.paymentdomain.model.entity.valueobject.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreatePaymentLinkCommand {
    private UUID bookingId;
    private BigDecimal amount;
    private List<InvoiceItem> items;
    private String note;
    private PaymentMethod method; // optional: để phân biệt cổng thanh toán nếu có nhiều
}
