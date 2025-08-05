package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    Invoice createInvoice(Invoice invoice, List<InvoiceItem> items);
    Invoice updateInvoice(Invoice invoice, List<InvoiceItem> items);
    void deleteInvoice(Invoice invoice);
    Optional<Invoice> findInvoiceById(Invoice invoice);
    Optional<Invoice> findByBookingId(UUID bookingId);
    List<Invoice> findAll(UUID customerId);
}
