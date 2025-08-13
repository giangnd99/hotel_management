package com.poly.paymentcontainer.dto2.invoiceitem;

import com.poly.paymentdataaccess.share.ServiceTypeEntity;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class CreateInvoiceItemRequest {
    private UUID serviceId;
    private String description;
    private ServiceTypeEntity serviceType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String note;
}
