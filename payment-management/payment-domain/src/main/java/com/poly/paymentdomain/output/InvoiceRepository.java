package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Invoice;

import java.util.List;

public interface InvoiceRepository {
    Invoice createInvoice(Invoice invoice);
    Invoice updateInvoice(Invoice invoice);
    void deleteInvoice(Invoice invoice);
    Invoice findInvoice(Invoice invoice);
    List<Invoice> findAllInvoices();
}
