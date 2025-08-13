package com.poly.paymentdomain.output;

import com.poly.paymentdomain.model.entity.InvoiceService;
import com.poly.paymentdomain.model.entity.InvoiceVoucher;

import java.util.List;
import java.util.UUID;

public interface InvoiceServiceRepository extends RepositoryGeneric<InvoiceService, UUID> {
    List<InvoiceService> findAllByBookingId(UUID bookingId);
}