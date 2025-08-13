package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.InvoicePayment;

import java.util.Optional;
import java.util.UUID;

public interface InvoicePaymentRepository extends RepositoryGeneric<InvoicePayment, UUID> {
    Optional<InvoicePayment> findByInvoiceId(UUID invoiceId);
}