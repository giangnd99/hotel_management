package com.poly.payment.management.application.rest.controller;

import com.poly.payment.management.domain.dto.request.CreateInvoiceCommand;
import com.poly.payment.management.domain.port.input.CreateInvoiceUsecase;
import com.poly.payment.management.domain.port.input.RetrieveInvoiceUsecase;
import com.poly.payment.dto.CreateInvoiceRequest;
import com.poly.payment.dto.InvoiceDetailRequest;
import com.poly.paymentdomain.model.value_object.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final CreateInvoiceUsecase createInvoiceUsecase;

    private final RetrieveInvoiceUsecase retrieveInvoiceUsecase;

    @PostMapping("/create")
    public ResponseEntity createInvoice(@RequestBody CreateInvoiceRequest request) {
        CreateInvoiceCommand command = CreateInvoiceCommand.builder()
                .referenceId(request.getReferenceId())
                .customerId(request.getCustomerId())
                .staffId(request.getStaffId())
                .voucherAmount(request.getVoucherAmount())
                .tax(request.getTax())
                .subTotal(request.getSubTotal())
                .totalAmount(request.getTotalAmount())
                .note(Description.from(request.getNote()))
                .build();
        return ResponseEntity.ok().body(createInvoiceUsecase.createInvoice(command));
    }

    @PostMapping("/detail")
    public ResponseEntity retrieveInvoice(@RequestBody InvoiceDetailRequest request) {
        return ResponseEntity.ok().body(retrieveInvoiceUsecase.retrieveInvoice(request.getInvoiceId()));
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
