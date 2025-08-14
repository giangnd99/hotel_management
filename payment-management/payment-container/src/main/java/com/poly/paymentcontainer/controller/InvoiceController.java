package com.poly.paymentcontainer.controller;

import com.poly.paymentapplicationservice.dto.command.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.port.input.CreateInvoiceUsecase;
import com.poly.paymentcontainer.dto.CreateInvoiceRequest;
import com.poly.paymentdomain.model.value_object.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final CreateInvoiceUsecase createInvoiceUsecase;

    @PostMapping("/create")
    public ResponseEntity createInvoice(@RequestBody CreateInvoiceRequest createInvoiceRequest) {
        CreateInvoiceCommand command = CreateInvoiceCommand.builder()
                .referenceId(createInvoiceRequest.getReferenceId())
                .customerId(createInvoiceRequest.getCustomerId())
                .staffId(createInvoiceRequest.getStaffId())
                .tax(createInvoiceRequest.getTax())
                .subTotal(createInvoiceRequest.getSubTotal())
                .totalAmount(createInvoiceRequest.getTotalAmount())
                .note(Description.from(createInvoiceRequest.getNote()))
                .build();
        return ResponseEntity.ok().body(createInvoiceUsecase.createInvoice(command));
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
