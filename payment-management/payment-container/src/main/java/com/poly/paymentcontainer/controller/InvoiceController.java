package com.poly.paymentcontainer.controller;

import com.poly.paymentapplicationservice.command.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.command.CreateInvoiceItemCommand;
import com.poly.paymentapplicationservice.port.input.InvoiceUsecase;
import com.poly.paymentcontainer.dto.invoice.CreateInvoiceRequest;
import com.poly.paymentdomain.model.entity.valueobject.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceUsecase invoiceUsecase;

    @PostMapping("/create")
    public ResponseEntity createInvoice(@RequestBody CreateInvoiceRequest request) throws Exception {
        CreateInvoiceCommand command = CreateInvoiceCommand.builder()
                .bookingId(request.getBookingId())
                .customerId(request.getCustomerId())
                .staffIdCreated(request.getStaffIdCreated())
                .voucherId(request.getVoucherId())
                .amountVoucher(request.getAmountVoucher())
                .taxAmount(request.getTaxAmount())
                .invoiceItemCommandList(request.getInvoiceItems()
                        .stream()
                        .map(item -> CreateInvoiceItemCommand.builder()
                                .serviceId(item.getServiceId())
                                .description(item.getDescription())
                                .serviceType(ServiceType.from(item.getServiceType().name()))
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .note(item.getNote())
                                .build()).collect(Collectors.toList()))
                .build();
        return ResponseEntity.ok().body(invoiceUsecase.makeInvoice(command));
    }
}
