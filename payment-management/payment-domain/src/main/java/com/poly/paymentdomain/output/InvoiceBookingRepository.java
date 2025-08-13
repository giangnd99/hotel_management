package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.InvoiceBooking;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceBookingRepository extends RepositoryGeneric<InvoiceBooking, UUID> {
    Optional<InvoiceBooking> findByInvoiceId(UUID invoiceId);
    Optional<InvoiceBooking> findByBookingId(UUID invoiceId);
}