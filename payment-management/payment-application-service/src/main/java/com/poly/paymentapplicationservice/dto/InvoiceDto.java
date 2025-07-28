package com.poly.paymentapplicationservice.dto;

import com.poly.paymentapplicationservice.command.CreateInvoiceItemCommand;
import com.poly.paymentdomain.model.entity.valueobject.InvoiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class InvoiceDto {
    private UUID id;
    private UUID bookingId;
    private UUID staffIdCreated;
    private UUID staffIdUpdated;
    private UUID voucherId;
    private BigDecimal subTotal;
    private BigDecimal total;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private InvoiceStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    List<InvoiceItemDto> items;
    List<PaymentDto> payments;
    private String note;

//    public static InvoiceDto from(CreateInvoiceItemCommand invoice) {
//        InvoiceDto invoiceDto = new InvoiceDto();
//        invoiceDto.id = UUID.randomUUID();
//    }

}
