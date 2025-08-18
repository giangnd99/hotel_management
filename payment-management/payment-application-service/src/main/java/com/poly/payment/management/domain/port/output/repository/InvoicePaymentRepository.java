package com.poly.payment.management.domain.port.output.repository;

import com.poly.payment.management.domain.model.InvoicePayment;

import java.util.Optional;
import java.util.UUID;

public interface InvoicePaymentRepository extends RepositoryGeneric<InvoicePayment, UUID> {
    Optional<InvoicePayment> findByPaymentId(UUID paymentId);

    Optional<InvoicePayment> findByInvoiceId(UUID invoiceId);
}