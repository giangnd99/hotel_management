package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceItem;

import java.util.List;

public interface InvoiceItemRepository {
    InvoiceItem save(InvoiceItem invoiceItem);
    InvoiceItem update(InvoiceItem invoiceItem);
    InvoiceItem findInvoice(Invoice invoice);
    void deleteInvoice(Invoice invoice);
    List<InvoiceItem> findAllInvoices();
}
