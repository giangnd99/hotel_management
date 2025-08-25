package com.poly.payment.management.application.rest.controller;


import com.poly.payment.management.domain.dto.request.CreateInvoiceCommand;
import com.poly.payment.management.domain.dto.request.CreateInvoiceRequest;
import com.poly.payment.management.domain.dto.request.InvoiceDetailRequest;
import com.poly.payment.management.domain.dto.response.InvoiceResult;
import com.poly.payment.management.domain.model.Invoice;
import com.poly.payment.management.domain.port.input.service.CreateInvoiceUsecase;
import com.poly.payment.management.domain.port.input.service.RetrieveAllInvoice;
import com.poly.payment.management.domain.port.input.service.RetrieveInvoiceUsecase;
import com.poly.payment.management.domain.value_object.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final CreateInvoiceUsecase createInvoiceUsecase;

    private final RetrieveInvoiceUsecase retrieveInvoiceUsecase;

    private final RetrieveAllInvoice retrieveAllInvoice;

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
    public ResponseEntity<InvoiceResult> retrieveInvoice(@RequestBody InvoiceDetailRequest request) {
        return ResponseEntity.ok().body(retrieveInvoiceUsecase.retrieveInvoice(request.getInvoiceId()));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> retrieveAllInvoice() {
        return ResponseEntity.ok(retrieveAllInvoice.retrieveAllInvoice());
    }
}
