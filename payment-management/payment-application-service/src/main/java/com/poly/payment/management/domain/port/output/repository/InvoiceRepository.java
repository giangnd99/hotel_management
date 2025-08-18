package com.poly.payment.management.domain.port.output.repository;

import com.poly.payment.management.domain.model.Invoice;

import java.util.UUID;

public interface InvoiceRepository extends RepositoryGeneric<Invoice, UUID> {}