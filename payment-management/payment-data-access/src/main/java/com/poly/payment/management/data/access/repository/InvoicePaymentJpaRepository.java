package com.poly.payment.management.data.access.repository;

import com.poly.payment.management.data.access.entity.InvoicePaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InvoicePaymentJpaRepository extends JpaRepository<InvoicePaymentEntity, UUID> {
    Optional<InvoicePaymentEntity> findByPaymentId(UUID paymentId);
    Optional<InvoicePaymentEntity> findByInvoiceId(UUID invoiceId);
}
