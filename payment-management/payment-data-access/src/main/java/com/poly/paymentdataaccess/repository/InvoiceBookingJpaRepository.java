package com.poly.paymentdataaccess.repository;

import com.poly.paymentdataaccess.entity.InvoiceBookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoiceBookingJpaRepository extends JpaRepository<InvoiceBookingEntity, UUID> {
    Optional<InvoiceBookingEntity> findByBookingId(UUID bookingId);
    Optional<InvoiceBookingEntity> findByInvoiceId(UUID invoiceId);
}
