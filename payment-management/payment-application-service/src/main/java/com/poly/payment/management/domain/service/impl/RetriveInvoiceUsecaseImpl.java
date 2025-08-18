package com.poly.payment.management.domain.service.impl;

import com.poly.payment.management.domain.dto.response.InvoiceResult;
import com.poly.payment.management.domain.exception.ApplicationServiceException;
import com.poly.payment.management.domain.port.input.service.RetrieveInvoiceUsecase;
import com.poly.payment.management.domain.model.Invoice;
import com.poly.payment.management.domain.port.output.repository.InvoiceRepository;

import java.util.Optional;
import java.util.UUID;

public class RetriveInvoiceUsecaseImpl implements RetrieveInvoiceUsecase {

    private final InvoiceRepository invoiceRepository;

    public RetriveInvoiceUsecaseImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public InvoiceResult retrieveInvoice(UUID invoiceId) {

        if (invoiceId == null) {
            throw new ApplicationServiceException("Invoice id cannot be null or invalid");
        }

        Optional<Invoice> existedInvoice = invoiceRepository.findById(invoiceId);

        if (!existedInvoice.isEmpty()) {
            throw new ApplicationServiceException("Invoice not found");

        }

        if (existedInvoice.isPresent()) {
            Invoice invoice = existedInvoice.get();
            return InvoiceResult.builder()
                    .invoiceId(invoice.getId().getValue())
                    .customerId(invoice.getCustomerId().getValue())
                    .staffId(invoice.getStaffId().getValue())
                    .status(invoice.getStatus().name())
                    .subAmount(invoice.getSubTotal().getValue())
                    .totalAmount(invoice.getTotalAmount().getValue())
                    .taxRate(invoice.getTaxRate().getValue())
                    .createdAt(invoice.getCreatedAt())
                    .updatedAt(invoice.getUpdatedAt())
                    .build();
        }
        throw new ApplicationServiceException("Invoice not found");
    }
}
