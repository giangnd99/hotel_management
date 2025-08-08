package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceItemRepository {
    InvoiceItem save(InvoiceItem invoiceItem);
    InvoiceItem update(InvoiceItem invoiceItem);
//    Optional<InvoiceItem> findInvoiceByInvoiceId(Invo invoice);
    void deleteInvoice(Invoice invoice);
    List<InvoiceItem> findByInvoiceId(UUID InvoiceId);
}
