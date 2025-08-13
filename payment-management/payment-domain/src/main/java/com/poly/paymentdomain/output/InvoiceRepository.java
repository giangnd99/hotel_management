package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.Invoice;

import java.util.UUID;

public interface InvoiceRepository extends RepositoryGeneric<Invoice, UUID> {}