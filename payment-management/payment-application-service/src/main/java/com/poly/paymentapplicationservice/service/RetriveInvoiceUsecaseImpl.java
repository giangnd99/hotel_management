package com.poly.paymentapplicationservice.service;

import com.poly.paymentapplicationservice.dto.result.InvoiceResult;
import com.poly.paymentapplicationservice.exception.ApplicationServiceException;
import com.poly.paymentapplicationservice.port.input.RetrieveInvoiceUsecase;
import com.poly.paymentdomain.model.Invoice;
import com.poly.paymentdomain.output.InvoiceRepository;

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
