package com.poly.payment.management.domain.service.impl;

import com.poly.payment.management.domain.model.Invoice;
import com.poly.payment.management.domain.port.input.service.RetrieveAllInvoice;
import com.poly.payment.management.domain.port.output.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RetrieveAllInvoiceImpl implements RetrieveAllInvoice {

    private final InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> retrieveAllInvoice() {
        return invoiceRepository.findAll();
    }
}
