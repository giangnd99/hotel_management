package com.poly.paymentcontainer.dto.invoice;

import com.poly.paymentcontainer.dto.invoiceitem.CreateInvoiceItemRequest;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class CreateInvoiceRequest {
    private UUID bookingId;
    private UUID customerId;
    private UUID staffIdCreated;
    private UUID voucherId;
    private BigDecimal amountVoucher;
    private List<CreateInvoiceItemRequest> invoiceItems;
}
