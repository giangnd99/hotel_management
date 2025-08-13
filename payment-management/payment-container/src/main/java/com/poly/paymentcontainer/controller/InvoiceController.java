package com.poly.paymentcontainer.controller;

import com.poly.paymentapplicationservice.dto.command.invoice.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.port.input.ok.CreateFinalInvoiceUseCase;
import com.poly.paymentcontainer.dto.CreateInvoiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final CreateFinalInvoiceUseCase invoiceUseCase;

    public ResponseEntity createInvoice(@RequestBody CreateInvoiceRequest createInvoiceRequest) {
        CreateInvoiceCommand command = CreateInvoiceCommand.builder()

                .build();
        return ResponseEntity.ok().body(invoiceUseCase.createInvoice(command));
    }

//    @PostMapping("/create")
//    public ResponseEntity createInvoice(@RequestBody CreateInvoiceRequest request) throws Exception {
//        CreateInvoiceCommand command = CreateInvoiceCommand.builder()
//                .bookingId(request.getBookingId())
//                .customerId(request.getCustomerId())
//                .staffIdCreated(request.getStaffIdCreated())
//                .voucherId(request.getVoucherId())
//                .amountVoucher(request.getAmountVoucher())
//                .taxAmount(request.getTaxAmount())
//                .invoiceItemCommandList(request.getInvoiceItems()
//                        .stream()
//                        .map(item -> CreateInvoiceItemCommand.builder()
//                                .serviceId(item.getServiceId())
//                                .description(item.getDescription())
//                                .quantity(item.getQuantity())
//                                .unitPrice(item.getUnitPrice())
//                                .note(item.getNote())
//                                .build()).collect(Collectors.toList()))
//                .build();
//        return ResponseEntity.ok().body(invoiceUsecase.makeInvoice(command));
//    }
}
