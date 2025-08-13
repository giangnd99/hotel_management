package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.InvoiceServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceServiceJpaRepository extends JpaRepository<InvoiceServiceEntity, UUID> {
    List<InvoiceServiceEntity> findAllByInvoiceBookingId(UUID invoiceId);
}
