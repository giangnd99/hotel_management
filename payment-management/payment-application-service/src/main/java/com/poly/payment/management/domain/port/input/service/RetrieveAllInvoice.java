package com.poly.payment.management.domain.port.input.service;

import com.poly.payment.management.domain.model.Invoice;

import java.util.List;

public interface RetrieveAllInvoice {
    List<Invoice> retrieveAllInvoice();
}
